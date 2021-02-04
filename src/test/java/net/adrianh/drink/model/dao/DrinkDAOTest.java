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
    
    @Before
    public void init() {
        
        Drink drink1 = new Drink(null,"drink", "a good description",null);
        Drink drink2 = new Drink(null,"drink 2", "a good description",null);
        drinkDAO.create(drink1);
        drinkDAO.create(drink2);
        drinkDAO.create(new Drink(null,"Margarita", "a good description",null));
        drinkDAO.create(new Drink(null,"Margarita", "a good description",null));
        ingredientDAO.create(new Ingredient(null,"Rum", Ingredient.Unit.CENTILITRE,6.0,42.0,drink1));
        ingredientDAO.create(new Ingredient(null,"Coke", Ingredient.Unit.CENTILITRE,12.0,0.0,drink1));
        Ingredient ingredient = new Ingredient(null,"Coke 2", Ingredient.Unit.CENTILITRE,12.0,0.0,drink2);
        ingredientDAO.create(ingredient);
        drink2.setIngredients(Arrays.asList(ingredient));
        drinkDAO.update(drink2);
        
    }
    
    
    @Test
    public void checkThatDrinkContainsIngredients() {
        System.out.println("hej hej");
        System.out.println(drinkDAO.findDrinksMatchingName("drink 2").get(0).toString());
        System.out.println(drinkDAO.findDrinksMatchingName("drink 2").get(0).getIngredients().size());
        Assert.assertTrue(drinkDAO.findDrinksMatchingName("drink 2").get(0).getIngredients().get(0).getName().equals("Coke 2"));
    }
    
    @Test
    public void checkThatAddWorked(){
        Assert.assertTrue(drinkDAO.findAll().size() == 4); 
    }

    
    @Test
    public void checkThatFindDrinksMatchingNameMatchesCorrectly() {
        Assert.assertTrue(drinkDAO.findDrinksMatchingName("Margarita").size() == 2);
    }
}
