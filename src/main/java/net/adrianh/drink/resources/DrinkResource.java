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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.adrianh.drink.model.dao.DrinkDAO;
import net.adrianh.drink.model.dao.IngredientDAO;
import net.adrianh.drink.model.dao.UserDAO;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.Ingredient;
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
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDrink(Drink d) {
        d.setUser(userDAO.findUserByID(1L).get(0)); 
        for (Ingredient i: d.getIngredients()) {
            i.setDrink(d);
        }
        drinkDAO.create(d);
        return Response.status(Response.Status.OK).build();
    }
    
    @POST
    @Path("popular")
    @Consumes("*/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(String acr) throws JSONException {
        
        JSONObject o = new JSONObject(acr);
        
        if("[]".equals(o.getString("queries"))) { //if no search, BRING ME THE BEST YOU HAVE
            List<Drink> allDrinks = new ArrayList<>();
            
            QueryResults qr = drinkDAO.findMostPopularFromOffset((o.getInt("offset")));
            allDrinks.addAll(qr.getResults());
            DrinkResponse drinks = new DrinkResponse((int) qr.getTotal(), allDrinks);
            
            return Response.status(Response.Status.OK).entity(drinks).build();
            
        } else { //if there is a search term, BRING ME THE BEST OF THEM
        
            Set<Drink> allDrinks = new HashSet<>();

            for(int i = 0; i < o.getJSONArray("queries").length(); i++) {
                if("drink".equals(o.getJSONArray("queries").getJSONObject(i).getString("type"))) {
                    allDrinks.addAll(drinkDAO.findDrinksMatchingName(o.getJSONArray("queries").getJSONObject(i).getString("name")));
                } else {
                    allDrinks.addAll(ingredientDAO.findDrinksFromIngredient(o.getJSONArray("queries").getJSONObject(i).getString("name")));
                }
            }

            List<Drink> selectedDrinks = new ArrayList<>();
            selectedDrinks.addAll(allDrinks);

            Collections.sort(selectedDrinks, new Comparator<Drink>(){
                @Override
                public int compare(Drink d1, Drink d2) {
                    return d2.getVoteCount() - d1.getVoteCount();
                }
            });

            return Response.status(Response.Status.OK).entity(selectedDrinks).build();
        }
    }
    
}
