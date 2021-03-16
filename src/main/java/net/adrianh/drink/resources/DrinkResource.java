package net.adrianh.drink.resources;

import com.querydsl.core.QueryResults;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.adrianh.drink.authorization.Secured;
import net.adrianh.drink.model.dao.DrinkDAO;
import net.adrianh.drink.model.dao.IngredientDAO;
import net.adrianh.drink.model.dao.UserDAO;
import net.adrianh.drink.model.dao.VoteDAO;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.Ingredient;
import net.adrianh.drink.model.entity.User;
import net.adrianh.drink.model.entity.Vote;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("drinks")
public class DrinkResource {

    @Data
    @XmlRootElement
    public class AutoCompleteResponse {

        private String type;
        private String name;
        private int offset;
    }

    @Data
    @XmlRootElement
    @AllArgsConstructor
    public class DrinkResponse {

        private int total;
        private List<Drink> drinks;
    }

    @EJB
    DrinkDAO drinkDAO;
    @EJB
    IngredientDAO ingredientDAO;
    @EJB
    UserDAO userDAO;
    @EJB
    VoteDAO voteDAO;

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDrink(Drink d, @Context SecurityContext securityContext) {
        // Get the name of the authorized user (derived from a valid token)
        User authorizedUser = userDAO.findUserByName(securityContext.getUserPrincipal().getName()).get(0);

        d.setUser(authorizedUser);
        for (Ingredient i : d.getIngredients()) {
            i.setDrink(d);
        }
        drinkDAO.create(d);
        return Response.status(Response.Status.OK).build();
    }


    /*
    @POST
    @Secured
    @Path("/mydrinks") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserCreatedDrinks(@Context SecurityContext securityContext) {
        User authorizedUser = userDAO.findUserByName(securityContext.getUserPrincipal().getName()).get(0);
        List<Drink> userDrinks = drinkDAO.findCreatedDrinksByUser(authorizedUser.getAccountName());
        return Response.status(Response.Status.OK).entity(userDrinks).build();
    }

 
    @POST
    @Secured
    @Path("/upvoted")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserUpvotedDrinks(@Context SecurityContext securityContext) {
        User authorizedUser = userDAO.findUserByName(securityContext.getUserPrincipal().getName()).get(0);
        List<Vote> userUpvotes = drinkDAO.findUpvotedDrinksByUser(authorizedUser.getAccountName());
        return Response.status(Response.Status.OK).entity(userUpvotes).build();
    }
     */

    @POST
    @Path("popular")
    @Secured // NOTE: The filter will still allow access to this particular endpoint even without proper auth header since auth is optional here
    @Consumes("*/*")
    @Produces(MediaType.APPLICATION_JSON)
    // TODO: Add query param for getting upvotes & implement it in resources and DAO
    public Response listPopular(String acr, @QueryParam("upvoted") boolean getUpvotedDrinks, @QueryParam("createdOnly") boolean getCreatedDrinks, @Context SecurityContext securityContext, @Context ContainerRequestContext requestContext) throws JSONException {

        JSONObject o = new JSONObject(acr);
        String authorizedUserName = null;
        // If the request is for a specific user's created drinks / upvotes, try to get the authorized user.
        // Will return UNAUTHORIZED if no PrincipalUuser exists (i.e if no auth header was provided)
        if (getCreatedDrinks || getUpvotedDrinks || requestContext.getHeaderString("Authorization") != null) {
            try {
                authorizedUserName = userDAO.findUserByName(securityContext.getUserPrincipal().getName()).get(0).getAccountName();
            } catch (NullPointerException e) {
                return Response.status(Response.Status.fromStatusCode(401)).build();
            }
        }
        if ("[]".equals(o.getString("queries"))) { //if no search, BRING ME THE BEST YOU HAVE
            List<Drink> allDrinks = new ArrayList<>();

            QueryResults qr = drinkDAO.findMostPopularFromOffset((o.getInt("offset")), authorizedUserName, getUpvotedDrinks, getCreatedDrinks);
            
            // If request is from an authorized user, append the user's individual vote status to each drink
            if (authorizedUserName != null) {
                putVoteStatuses((List<Drink>)qr.getResults(), authorizedUserName);
            }

            allDrinks.addAll(qr.getResults());
            DrinkResponse drinks = new DrinkResponse((int) qr.getTotal(), allDrinks);

            return Response.status(Response.Status.OK).entity(drinks).build();

        } else { //if there is a search term, BRING ME THE BEST OF THEM

            Set<Drink> allDrinks = new HashSet<>();
            int total = 0;

            if ("drink".equals(o.getJSONArray("queries").getJSONObject(0).getString("type"))) {
                QueryResults dr = drinkDAO.findDrinksMatchingNameFromOffset(o.getJSONArray("queries").getJSONObject(0).getString("name"), o.getInt("offset"), authorizedUserName, getUpvotedDrinks, getCreatedDrinks);
                if (authorizedUserName != null) {
                    putVoteStatuses((List<Drink>)dr.getResults(),authorizedUserName);
                }
                allDrinks.addAll(dr.getResults());
                total = (int) dr.getTotal();
            } else {
                QueryResults ir = null;
                for (int i = 0; i < o.getJSONArray("queries").length(); i++) {
                    ir = ingredientDAO.findDrinksFromIngredientsMatchingNameFromOffset(o.getJSONArray("queries").getJSONObject(i).getString("name"), o.getInt("offset"), authorizedUserName, getUpvotedDrinks, getCreatedDrinks);
                    
                    if (authorizedUserName != null) {
                        putVoteStatuses((List<Drink>)ir.getResults(),authorizedUserName);
                    }
                    
                    if (allDrinks.isEmpty()) {
                        
                        allDrinks.addAll(ir.getResults());
                        total = (int) ir.getTotal();
                    } else {
                        allDrinks.retainAll(ir.getResults());
                    }
                    if (total > ir.getTotal()) {
                        total = (int) ir.getTotal();
                    }
                    //this isn't a real solution, however due to time constraints this will have to suffice
                }
            }

            List<Drink> selectedDrinks = new ArrayList<>();
            selectedDrinks.addAll(allDrinks);

            Collections.sort(selectedDrinks, new Comparator<Drink>() {
                @Override
                public int compare(Drink d1, Drink d2) {
                    return d2.getVoteCount() - d1.getVoteCount();
                }
            });

            DrinkResponse drinkResponse = new DrinkResponse(total, selectedDrinks);
            return Response.status(Response.Status.OK).entity(drinkResponse).build();
        }
    }

    @POST
    @Secured // NOTE: The filter will still allow access to this particular endpoint even without proper auth header since auth is optional here
    @Path("newest")
    @Consumes("*/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listNewest(String acr, @QueryParam("upvoted") boolean getUpvotedDrinks, @QueryParam("createdOnly") boolean getCreatedDrinks, @Context SecurityContext securityContext, @Context ContainerRequestContext requestContext) throws JSONException {

        JSONObject o = new JSONObject(acr);

        String authorizedUserName = null;
        if (getCreatedDrinks || getUpvotedDrinks || requestContext.getHeaderString("Authorization") != null) {
            try {
                authorizedUserName = userDAO.findUserByName(securityContext.getUserPrincipal().getName()).get(0).getAccountName();
            } catch (NullPointerException e) {
                return Response.status(Response.Status.fromStatusCode(401)).build();
            }
        }

        if ("[]".equals(o.getString("queries"))) { //if no search, BRING ME THE BEST YOU HAVE
            List<Drink> allDrinks = new ArrayList<>();

            QueryResults qr = drinkDAO.findNewestFromOffset((o.getInt("offset")), authorizedUserName, getUpvotedDrinks, getCreatedDrinks);

            if (authorizedUserName != null) {
                putVoteStatuses((List<Drink>)qr.getResults(), authorizedUserName);
            }
            allDrinks.addAll(qr.getResults());
            DrinkResponse drinks = new DrinkResponse((int) qr.getTotal(), allDrinks);

            return Response.status(Response.Status.OK).entity(drinks).build();

        } else { //if there is a search term, BRING ME THE BEST OF THEM

            Set<Drink> allDrinks = new HashSet<>();
            int total = 0;

            if ("drink".equals(o.getJSONArray("queries").getJSONObject(0).getString("type"))) {
                QueryResults dr = drinkDAO.findDrinksMatchingNameFromOffsetByNewest(o.getJSONArray("queries").getJSONObject(0).getString("name"), o.getInt("offset"), authorizedUserName, getUpvotedDrinks, getCreatedDrinks);
                
                if (authorizedUserName != null) {
                    putVoteStatuses((List<Drink>)dr.getResults(), authorizedUserName);
                }
                
                allDrinks.addAll(dr.getResults());
                total = (int) dr.getTotal();
            } else {
                QueryResults ir = null;
                for (int i = 0; i < o.getJSONArray("queries").length(); i++) {
                    ir = ingredientDAO.findDrinksFromIngredientsMatchingNameFromOffsetByNewest(o.getJSONArray("queries").getJSONObject(i).getString("name"), o.getInt("offset"), authorizedUserName, getUpvotedDrinks, getCreatedDrinks);
                    
                    if (authorizedUserName != null) {
                        putVoteStatuses((List<Drink>)ir.getResults(), authorizedUserName);
                    }
                    
                    if (allDrinks.isEmpty()) {

                        allDrinks.addAll(ir.getResults());
                        total = (int) ir.getTotal();
                    } else {
                        allDrinks.retainAll(ir.getResults());
                    }
                    if (total > ir.getTotal()) {
                        total = (int) ir.getTotal();
                    }
                    //this isn't a real solution, however due to time constraints this will have to suffice
                }
            }

            List<Drink> selectedDrinks = new ArrayList<>();
            selectedDrinks.addAll(allDrinks);

            Collections.sort(selectedDrinks, new Comparator<Drink>() {
                @Override
                public int compare(Drink d1, Drink d2) {
                    return d2.getCreatedAt().compareTo(d1.getCreatedAt());
                }
            });

            DrinkResponse drinkResponse = new DrinkResponse(total, selectedDrinks);
            return Response.status(Response.Status.OK).entity(drinkResponse).build();
        }
    }

    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response drinkFromId(@PathParam("id") Long Id) throws JSONException {
        return Response.status(Response.Status.OK).entity(drinkDAO.findDrinkByID(Id)).build();
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response removeDrink(@PathParam("id") Long Id, @Context SecurityContext securityContext) {
        // Get the name of the authorized user (derived from a valid token)
        User authorizedUser = userDAO.findUserByName(securityContext.getUserPrincipal().getName()).get(0);
        if(drinkDAO.findDrinkByID(Id).getUser().getId().equals(authorizedUser.getId())){
            drinkDAO.remove(drinkDAO.findDrinkByID(Id));
            return Response.status(Response.Status.OK).entity("Succesfully deleted!").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Not your drink!").build(); 
        }
    }
    
    @GET
    @Path("brave")
    @Produces(MediaType.APPLICATION_JSON)
    public Response iAmFeelingBrave(){
        double total = drinkDAO.findMostPopularFromOffset(0,null,false,false).getTotal();
        total = Math.floor(Math.random() * total);
        Drink drink = drinkDAO.findMostPopularFromOffset((int) total,null,false,false).getResults().get(0);
        return Response.status(Response.Status.OK).entity(drink).build();
    }
    
    // Iterates over the fetched list of drinks and sets the voteStatus field of every drink to 1, 0 or -1 depending on
    // the current user's vote on the specified drink
    private void putVoteStatuses(List<Drink> drinks, String username) {
        User user = userDAO.findUserByName(username).get(0);
        for (Drink d : drinks) {
            d.setVoteStatus(voteDAO.hasUserVotedDrink(user.getId(), d.getId()) ? voteDAO.selectVote(user.getId(), d.getId()).getVal() : 0);
        }
    }
}
