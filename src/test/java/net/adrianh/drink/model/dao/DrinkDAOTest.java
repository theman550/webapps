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
import java.util.Date;

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
    private UserDAO userDAO;
    
    @Before
    public void init() {
        User usr = new User(1L,"man","usr", "pw", "salt", null, null);
        
        usr.setCreatedDrinks(new ArrayList<>());
        usr.setVotes(new ArrayList<>());
	Drink d = new Drink();
        d.setName("Margarita");
        d.setVoteCount(1);
        
        Drink d2 = new Drink();
        d2.setName("d2");
        d2.setVoteCount(2);
        
        Drink d3 = new Drink();
        d3.setName("d3");
        d3.setVoteCount(3);
        
        Drink d4 = new Drink();
        d4.setName("d4");
        d4.setVoteCount(4);
        
        Drink d5 = new Drink(); //DIFFERENT DRINK, SAME NAME
        d5.setName("d4");
        d5.setVoteCount(5);

	d.setUser(usr);
	usr.addDrink(d);
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
    // True om det finns någon drink alls
    public void checkThatAddWorks(){
        Assert.assertTrue(drinkDAO.findAll().size() > 0); 
    }
    
    @Test
    //True om det går att hitta en user från en drink
    public void checkThatFindUserByDrinkWorks(){ 
	Drink d = drinkDAO.findAll().get(0);
	User usr = d.getUser();
	Assert.assertTrue(usr.getCreatedDrinks().contains(d));
    }

    @Test
    //True om det finns minst en drink som heter Margarita
    public void checkThatGetDrinkByNameWorks(){
	List<Drink> margaritas = drinkDAO.findDrinksMatchingName("Margarita");
	Assert.assertTrue(margaritas.get(0).getName().equals("Margarita"));
    } 
    
    @Test
    //True if autocomplete can find a drink starting with the serach term
    public void checkThatAutoCompleteWorks() {
        List<Drink> margaritas = drinkDAO.findDrinksStartMatchingName("Mar");
	Assert.assertTrue(margaritas.get(0).getName().equals("Margarita"));
    }
    
    @Test
    //True if drinks[0] is the 3rd most popular drink from all added drinks (drink name "d3")
    public void checkThatMostPopularFromOffsetWorks() {
        List<Drink> drinks = drinkDAO.findMostPopularFromOffset(2).getResults();
        Assert.assertTrue(drinks.get(0).getName().equals("d3"));
    }
    
    @Test
    //True if drinks[0] is the 2nd most popular drink from all added drinks that start with "d" (drink name "d4" with 4 votes)
    public void checkThatFindDrinksMatchingNameFromOffsetWorks() {
        List<Drink> drinks = drinkDAO.findDrinksMatchingNameFromOffset("d4", 1).getResults();
        Assert.assertTrue(drinks.get(0).getName().equals("d4"));
        Assert.assertEquals(4, drinks.get(0).getVoteCount());
    }
    
    @Test
    //True if drinks[0] is the 4th newest drink from all added drinks (drink name "d2")
    //Due to it being impossible to predict in what order the drinks are created, this test WILL fail from time to time
    public void checkThatFindNewestFromOffset() {
        List<Drink> drinks = drinkDAO.findNewestFromOffset(3).getResults();
        Assert.assertEquals("d2", drinks.get(0).getName());
    }
   
    @After
    public void clean(){
        List <User> users = userDAO.findAll();
        for (User u : users) {
            userDAO.remove(u);
        }
    }
}
