package net.adrianh.drink.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
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
import net.adrianh.drink.model.entity.Drink;
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
    }
    
    @EJB
    DrinkDAO drinkDAO;
    @EJB
    IngredientDAO ingredientDAO;
    
    @POST
    @Path("popular")
    @Consumes("*/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(String acr) throws JSONException {
        
        if("[]".equals(acr)) { //if no search, BRING ME THE BEST YOU HAVE
            List<Drink> allDrinks = new ArrayList<>();
            
            allDrinks.addAll(drinkDAO.findAll());
            
            // Calculate the voteCount field
            for (Drink d : allDrinks) {
                d.setVoteCount(d.getVotes().stream().reduce(0,(a,b) -> a + b.getVal(), Integer::sum));
            }
            
            Collections.sort(allDrinks, new Comparator<Drink>(){
                @Override
                public int compare(Drink d1, Drink d2) {
                    return d1.getVoteCount() - d2.getVoteCount();
                }
            });
            
            return Response.status(Response.Status.OK).entity(allDrinks).build();
            
        } else { //if there is a search term, BRING ME THE BEST OF THEM
        
            JSONArray o = new JSONArray(acr);
            Set<Drink> allDrinks = new HashSet<>();

            for(int i = 0; i < o.length(); i++) {
                if("drink".equals(o.getJSONObject(i).getString("type"))) {
                    allDrinks.addAll(drinkDAO.findDrinksMatchingName(o.getJSONObject(i).getString("name")));
                } else {
                    allDrinks.addAll(ingredientDAO.findDrinksFromIngredient(o.getJSONObject(i).getString("name")));
                }
            }

            List<Drink> selectedDrinks = new ArrayList<>();
            selectedDrinks.addAll(allDrinks);

            // Calculate the voteCount field
            for (Drink d : selectedDrinks) {
                d.setVoteCount(d.getVotes().stream().reduce(0,(a,b) -> a + b.getVal(), Integer::sum));
            }

            Collections.sort(selectedDrinks, new Comparator<Drink>(){
                @Override
                public int compare(Drink d1, Drink d2) {
                    return d1.getVoteCount() - d2.getVoteCount();
                }
            });

            return Response.status(Response.Status.OK).entity(selectedDrinks).build();
        }
    }
}
