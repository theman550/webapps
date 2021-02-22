package net.adrianh.drink.resources;


import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.adrianh.drink.model.dao.DrinkDAO;

@Path("autocomplete")
public class DrinkResources {
    
    @Data
    @AllArgsConstructor
    public class AutoCompleteResponse {
        private String type;
        private String name;
    }
    
    @EJB
    DrinkDAO drinkDAO;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("s") String s) {
        List<AutoCompleteResponse> r = drinkDAO.findDrinksStartMatchingName(s).stream().map((drink) -> new AutoCompleteResponse("drink",drink.getName())).collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(r).build();
    }
}