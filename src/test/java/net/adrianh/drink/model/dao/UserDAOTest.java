/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.model.dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.Ingredient;
import net.adrianh.drink.model.entity.User;
import net.adrianh.drink.model.entity.Vote;
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

/**
 *
 * @author Jacob Spilg
 */
@RunWith(Arquillian.class)
public class UserDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(DrinkDAO.class, Drink.class, IngredientDAO.class, Ingredient.class, User.class, UserDAO.class, Vote.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @EJB
    private UserDAO userDAO;
    
    @Before
    public void init() {
	User usr = new User(null,"usr","CoolGuy", "pw", "salt", null, null);
	usr.setCreatedDrinks(new ArrayList<>());
        usr.setVotes(new ArrayList<>());
	userDAO.create(usr);
    }
       
    // True if a user is created and the class is User (meh)
    @Test
    public void addUser(){
	List<User> usrs = userDAO.findAll();
	Assert.assertTrue(usrs.size() > 0 && usrs.get(0).getClass().equals(User.class));
    }
    
    @Test
    //True if the user is in the database with give password
    public void checkThatLoginWorks() {
        List<User> usrs = userDAO.findAll();
        Assert.assertEquals(usrs.get(0), userDAO.login("usr", "pw"));
    }
    
    @Test
    //Should return true if a given user exists and return false for one that doesn't exist
    public void checkThatAreCredentialsMatchingWorks() {
        Assert.assertTrue(userDAO.areCredentialsMatching("usr", "pw"));
        Assert.assertFalse(userDAO.areCredentialsMatching("idontexist", "andneitherdoi"));
    }
    
    @Test
    //Should return true if said account name is unique, otherwise return false
    public void checkThatIsAccNameUniquieWorks() {
        Assert.assertTrue(userDAO.isAccNameUnique("hello there"));
        Assert.assertFalse(userDAO.isAccNameUnique("usr"));
    }
    
    @Test
    //True if user "usr"'s id is the same
    public void checkThatFindUserByIDWorks() {
        List<User> usrs = userDAO.findAll();
        Assert.assertEquals(usrs.get(0).getAccountName(), userDAO.findUserByID(usrs.get(0).getId()).get(0).getAccountName());
    }
    
    @Test
    //True if the found salt is the same as "usr"'s
    public void checkThatFindSaltByNameWorks() {
        Assert.assertEquals("salt", userDAO.findSaltByName("usr"));
    }
    
    @Test
    //Should return a User if it exists and return nothing if a User doesn't exist
    public void checkThatFindUserByNameWorks() {
        List<User> usrs = userDAO.findAll();
        Assert.assertEquals(usrs.get(0), userDAO.findUserByName("usr").get(0));
        Assert.assertEquals(0, userDAO.findUserByName("idontexist").size());
    }
    
    @After
    public void clean(){
        List <User> users = userDAO.findAll();
	users.forEach(u -> {
	    userDAO.remove(u);
	});
    }
}












       /*
	User adrian = new User(null,"adrian", null, null);
        
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
        
        

        adrian.addVote(new Vote(null,null,drink1,1));
        
        userDAO.create(adrian);
        */
   