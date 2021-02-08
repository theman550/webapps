package net.adrianh.drink.model.dao;


import java.util.List;
import javax.ejb.EJB;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.Ingredient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import net.adrianh.drink.model.entity.User;
import net.adrianh.drink.model.entity.Vote;

@RunWith(Arquillian.class)
public class DrinkDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(DrinkDAO.class, Drink.class, IngredientDAO.class, Ingredient.class, User.class, UserDAO.class, Vote.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @EJB
    private DrinkDAO drinkDAO;
    @EJB
    private IngredientDAO ingredientDAO;
    @EJB
    private UserDAO userDAO;
    
    @Before
    public void init() {
        User adrian = new User(null,"adrian", null, null);
        adrian.setCreatedDrinks(new ArrayList<>());
        adrian.setVotes(new ArrayList<>());
        
        Drink drink1 = new Drink();
        drink1.setName("drink");
        drink1.setDescription("a good description");
        adrian.addDrink(drink1);
        
        Drink drink2 = new Drink();
        drink2.setName("drink 2");
        adrian.addDrink(drink2);
        
        Drink drink3 = new Drink();
        drink3.setName("Margarita");
        adrian.addDrink(drink3);
        
        drink1.addIngredient(new Ingredient("Rum", Ingredient.Unit.CENTILITRE,6.0,42.0,null));
        drink1.addIngredient(new Ingredient("Coke", Ingredient.Unit.CENTILITRE,12.0,0.0,null));
        
        drink2.addIngredient(new Ingredient("Coke", Ingredient.Unit.CENTILITRE,12.0,0.0,null));

        adrian.addVote(new Vote(null,null,drink1,1));
        
        userDAO.create(adrian);
        
    }
    
    @Test
    // True om det finns någon drink alls
    public void checkThatAddWorks(){
        Assert.assertTrue(drinkDAO.findAll().size() > 0); 
    }

    @Test
    //True om det finns mer än en drink med Coke 
    public void checkThatFindDrinksByIngredientWorks(){ 
	List<Drink> drinkar = ingredientDAO.findDrinksFromIngredient("Coke");
	Assert.assertTrue(drinkar.size() > 1);
    }

    @Test
    //True om det finns minst en drink som heter Margarita
    public void checkThatGetDrinkByNameWorks(){
	List<Drink> margaritas = drinkDAO.findDrinksMatchingName("Margarita");
	Assert.assertTrue(margaritas.get(0).getName().equals("Margarita"));
    } 
   
    @After
    public void clean(){
        List <User> users = userDAO.findAll();
        for (User u : users) {
            userDAO.remove(u);
        }
    }
}
