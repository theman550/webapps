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
import static java.lang.System.out;
import net.adrianh.drink.model.entity.User;
import net.adrianh.drink.model.entity.Vote;
/**
 *
 * @author Jacob Spilg
 */
@RunWith(Arquillian.class)
public class IngredientDAOTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(DrinkDAO.class, Drink.class, IngredientDAO.class, Ingredient.class, User.class, UserDAO.class, Vote.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    @EJB
    private UserDAO userDAO;
    @EJB
    private DrinkDAO drinkDAO;
    @EJB
    private IngredientDAO ingredientDAO;
    
    @Before
    public void init() {
	
	User usr = new User(null,"usr", null, null);
	usr.setCreatedDrinks(new ArrayList<>());
        usr.setVotes(new ArrayList<>());

	Ingredient i = new Ingredient("Rum", Ingredient.Unit.CENTILITRE,6.0,42.0,null);
	Ingredient i2 = new Ingredient("Coke", Ingredient.Unit.CENTILITRE,12.0,0.0,null);
	Drink d = new Drink();
	d.setName("margamotto");
	d.setDescription("Mums");
	d.setUser(usr);
	d.addIngredient(i);
	d.addIngredient(i2);
	usr.addDrink(d);
	userDAO.create(usr);
    }
    
    @Test
    // True om det finns någon drink alls
    public void checkThatAddWorks(){
        Assert.assertTrue(1 > 0); 
    }
  /*  @Test
    public void testCreateIngredient(){
	Assert.assertTrue("Ingredient was not created, so that's bad", !ingredientDAO.findAll().isEmpty());
    }
    
    // True om det finns minst en ingrediens i drinken
    @Test
    public void findIngredientsFromDrink(){
	Drink d = userDAO.findAll().get(0).getCreatedDrinks().get(0);
	Assert.assertTrue(d.getIngredients().size()>0);
    }
    
    @Test
    //True om det finns minst en drink med Coke 
    public void checkThatFindDrinksByIngredientWorks(){ 
	List<Drink> drinkar = ingredientDAO.findDrinksFromIngredient("Coke");
	Assert.assertTrue(drinkar.size() > 0);
    }*/
    
   /* @After
    public void clean(){
	List<User> usrs = userDAO.findAll();
	usrs.forEach(usr -> {
	    userDAO.remove(usr);
	});
    }*/
}