package net.adrianh.drink.resources;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import net.adrianh.drink.model.dao.DrinkDAO;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.QDrink;
import javax.persistence.EntityManager;

@Path("drinkre")
public class DrinkResource {
    
    @EJB
    private DrinkDAO drink;

    @GET // curl -X GET --basic http://localhost:8080/wsbooks/ws/shelf
    public List<Drink> list() {
        return drink.allDrinks();
    }

}
