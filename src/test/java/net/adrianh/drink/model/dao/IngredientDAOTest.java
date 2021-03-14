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
import java.util.Collections;
import java.util.Comparator;
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
	
	User usr = new User(null,"man","usr", "pw", "salt", null, null, null);
	usr.setCreatedDrinks(new ArrayList<>());
        usr.setVotes(new ArrayList<>());

	Ingredient ing = new Ingredient("Rum", Ingredient.Unit.CENTILITRE,6.0,42.0,null);
	Ingredient ing2 = new Ingredient("Coke", Ingredient.Unit.CENTILITRE,12.0,0.0,null);
        Ingredient ing3 = new Ingredient("Rum", Ingredient.Unit.CENTILITRE,6.0,42.0,null);
        Ingredient ing4 = new Ingredient("Rum", Ingredient.Unit.CENTILITRE,6.0,42.0,null);
	Drink d = new Drink();
        d.setIngredients(new ArrayList<>());
	d.setName("Margarita");
	d.setDescription("Mums");
	d.setUser(usr);
	d.addIngredient(ing);
	d.addIngredient(ing2);
	usr.addDrink(d);
        
        Drink d2 = new Drink();
        d2.setName("d2");
        d2.setVoteCount(2);
        d2.setIngredients(new ArrayList<>());
        d2.addIngredient(ing4);
        
        Drink d3 = new Drink();
        d3.setName("d3");
        d3.setVoteCount(3);
        d3.setIngredients(new ArrayList<>());
        d3.addIngredient(ing3);
        
        Drink d4 = new Drink();
        d4.setName("d4");
        d4.setVoteCount(4);
        
        Drink d5 = new Drink(); //DIFFERENT DRINK, SAME NAME
        d5.setName("d4");
        d5.setVoteCount(5);
        
        d2.setUser(usr);
	usr.addDrink(d2);
	d3.setUser(usr);
	usr.addDrink(d3);
	d4.setUser(usr);
	usr.addDrink(d4);
        d5.setUser(usr);
	usr.addDrink(d5);
        
	userDAO.create(usr);
    }
    
    
    @Test
    public void testCreateIngredient(){
	Assert.assertTrue("Ingredient was not created, so that's bad", !ingredientDAO.findAll().isEmpty());
    }
    
    // True om det finns minst en ingrediens i drinken
    @Test
    public void findIngredientsInDrink(){
	Drink d = userDAO.findAll().get(0).getCreatedDrinks().get(0);
	Assert.assertTrue(d.getIngredients().size()>0);
    }
    
    @Test
    //True om det finns minst en drink med Coke 
    public void checkThatFindDrinksFromIngredientWorks(){ 
	List<Drink> drinkar = ingredientDAO.findDrinksFromIngredient("Coke");
	Assert.assertTrue(drinkar.size() > 0);
    }
    
    @Test
    //True if there is a ingredient with the name "Coke"
    public void checkThatFindIngredientsMatchingNameWorks() {
        List<Ingredient> ing = ingredientDAO.findIngredientsMatchingName("Coke");
        Assert.assertTrue(ing.get(0).getName().equals("Coke"));
    }
    
    @Test
    //True if it can find Coke from serach term "co"
    public void checkThatFindIngredientsStartingWithWorks() {
        List<Ingredient> ing = ingredientDAO.findIngredientsStartingWith("co");
        Assert.assertTrue(ing.get(0).getName().equals("Coke"));
    }
    
    @Test
    //True if drinks[0] contains "d2", as it has a lower votecount than "d3" but same ingredient
    public void checkThatFindDrinksFromIngredientsMatchingNameFromOffsetWorks() {
        List<Drink> drinks = ingredientDAO.findDrinksFromIngredientsMatchingNameFromOffset("Rum",1,null).getResults();
        Assert.assertEquals("d2", drinks.get(0).getName());
    }
    
    @Test
    //True if drinks[0] is the newest drink that has the ingredient "Rum"
    //Seems as if it saves to the databse whenever it feels like it. The test will sometimes work and sometimes not. Welp, RIP[*]
    public void checkThatFindDrinksFromIngredientsMatchingNameFromOffsetByNewestWorks() {
        List<Drink> drinks = ingredientDAO.findDrinksFromIngredient("Rum");
        Collections.sort(drinks, new Comparator<Drink>(){ //sort drinks by date using a comparator
                @Override
                public int compare(Drink d1, Drink d2) {
                    return d2.getCreatedAt().compareTo(d1.getCreatedAt());
                }
        });
        Assert.assertEquals(drinks.get(1).getName(), ingredientDAO.findDrinksFromIngredientsMatchingNameFromOffsetByNewest("Rum", 1,null).getResults().get(0).getName());
    }
    
    @After
    public void clean(){
	List<User> usrs = userDAO.findAll();
	usrs.forEach(usr -> {
	    userDAO.remove(usr);
	});
    };
}