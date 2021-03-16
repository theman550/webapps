package net.adrianh.drink.resources;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.adrianh.drink.model.dao.DrinkDAO;
import net.adrianh.drink.model.dao.IngredientDAO;

@Path("autocomplete")
public class AutoComplete {

    @EJB
    DrinkDAO drinkDAO;
    @EJB
    IngredientDAO ingredientDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("s") String s) {
        Set<AutoCompleteResponse> r = new HashSet<>();

        // Add suggsetions for all drinks whose name match the query
        r.addAll(drinkDAO.findDrinksStartMatchingName(s).stream().map((drink) -> new AutoCompleteResponse("drink", drink.getName())).collect(Collectors.toList()));

        // Add suggestions for all drinks with ingredients matching the query
        r.addAll(ingredientDAO.findIngredientsStartingWith(s).stream().map((ingredient) -> new AutoCompleteResponse("ingredient", ingredient.getName())).collect(Collectors.toList()));
        return Response.status(Response.Status.OK).entity(r).build();
    }
}
