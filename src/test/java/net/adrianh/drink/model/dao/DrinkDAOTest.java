package net.adrianh.drink.model.dao;


import java.util.Arrays;
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
import static java.lang.System.out;
import net.adrianh.drink.model.entity.User;
import net.adrianh.drink.model.entity.Vote;

@RunWith(Arquillian.class)
public class DrinkDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(DrinkDAO.class, Drink.class, IngredientDAO.class, Ingredient.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @EJB
    private DrinkDAO drinkDAO;
    @EJB
    private IngredientDAO ingredientDAO;
    @EJB
    private UserDAO userDAO;
    @EJB
    private VoteDAO voteDAO;
    
    @Before
    public void init() {
        User adrian = new User("adrian", null, null);
        userDAO.create(adrian);
        Drink drink1 = new Drink(null,"drink", "a good description",null, null, adrian);
        Drink drink2 = new Drink(null,"drink 2", "a good description",null, null, null);
        Vote vote_adrian = new Vote(adrian, drink1, 1);
        voteDAO.create(vote_adrian);
        drinkDAO.create(drink1);
        drinkDAO.create(drink2);
        drinkDAO.create(new Drink(null,"Margarita", "a good description",null, null, adrian));
        drinkDAO.create(new Drink(null,"Margarita", "a good description",null, null, null));
        ingredientDAO.create(new Ingredient(null,"Rum", Ingredient.Unit.CENTILITRE,6.0,42.0,drink1));
        ingredientDAO.create(new Ingredient(null,"Coke", Ingredient.Unit.CENTILITRE,12.0,0.0,drink1));
        Ingredient ingredient = new Ingredient(null,"Coke", Ingredient.Unit.CENTILITRE,12.0,0.0,drink2);
        ingredientDAO.create(ingredient);
        drink2.setIngredients(Arrays.asList(ingredient));
        drinkDAO.update(drink2);
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
    

    /*@After
    public void clean(){
	List <Drink> drinkar = drinkDAO.findAll();
	for(Drink d: drinkar)
	    drinkDAO.remove(d);
    }*/
    
}
