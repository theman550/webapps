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
        Ingredient i = new Ingredient("Ost", Ingredient.Unit.LITRE,9.0,0.0,null);
	Ingredient i2 = new Ingredient("Salt", Ingredient.Unit.LITRE,15.0,0.0,null);
        
        User usr = new User(null,"usr", null, null);
        usr.setCreatedDrinks(new ArrayList<>());
        usr.setVotes(new ArrayList<>());
	Drink d = new Drink();
        d.setName("Margarita");
        d.setDescription("gooood");
        d.addIngredient(i);
	d.addIngredient(i2);
	d.setUser(usr);
	usr.addDrink(d);

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
   
    /*@After
    public void clean(){
        List <User> users = userDAO.findAll();
        for (User u : users) {
            userDAO.remove(u);
        }
    }*/
}
