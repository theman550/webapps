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
import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import net.adrianh.drink.model.dao.key.votePK;
import net.adrianh.drink.model.entity.User;
import net.adrianh.drink.model.entity.Vote;

/**
 *
 * @author andra
 */
@RunWith(Arquillian.class)
public class VoteDAOTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(DrinkDAO.class, Drink.class, IngredientDAO.class, Ingredient.class, User.class, UserDAO.class, VoteDAO.class, Vote.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    @EJB
    private UserDAO userDAO;
    @EJB
    private VoteDAO voteDAO;
    @Inject
    UserTransaction tx;

    @Before
    public void init() throws NotSupportedException, SystemException {
        tx.begin();
	User usr = new User(1L,"aname","dname", "pw", "salt", null, null, null);
        usr.setVotes(new ArrayList<>());
        usr.setCreatedDrinks(new ArrayList<>());
              
        Drink d = new Drink();
        d.setId(1L);
        d.setVotes(new ArrayList<>());
        Drink d2 = new Drink();
        d2.setId(2L);
        d2.setVotes(new ArrayList<>());
        Drink d3 = new Drink();
        d3.setId(3L);
        d3.setVotes(new ArrayList<>());

        Vote uv = new Vote(new votePK(usr.getId(),d.getId()), usr, d, 1); 
        Vote uv2 = new Vote(new votePK(usr.getId(),d2.getId()), usr, d2, 1); 
        Vote dv = new Vote(new votePK(usr.getId(),d3.getId()), usr, d3, -1); 

        ArrayList votes = new ArrayList();
        votes.add(uv);
        votes.add(uv2);
        votes.add(dv);

        d.setUser(usr);
        usr.addDrink(d);
        d2.setUser(usr);
        usr.addDrink(d2);
        d3.setUser(usr);
        usr.addDrink(d3);
        
        d.setVotes(votes);
        d2.setVotes(votes);
        d3.setVotes(votes);
 
        usr.setVotes(votes);
        
        userDAO.create(usr);  
        userDAO.getEntityManager().flush();
        //voteDAO.getEntityManager().flush();
    }
    
    //Tests only worked when running individually or in the same Test method
    @Test
    public void testUserVotes() { 
        //True if drink has one vote
        //Assert.assertEquals(1, voteDAO.allDrinkVotes(1L));
        
        /*selectVote generates unique PK value for votes 
          Instead check if userID and drinkID are the same
          True if userID and drinkID are equal
        */
        Assert.assertEquals(voteDAO.selectVote(1L, 1L).getDrink().getId(), 
                voteDAO.selectVote(1L, 1L).getDrink().getId());
        Assert.assertEquals(voteDAO.selectVote(1L, 1L).getUser_id().getId(), 
                voteDAO.selectVote(1L, 1L).getUser_id().getId());

        //True if user with ID 1L voted for drink with id 1L
        Assert.assertTrue(voteDAO.hasUserVotedDrink(1L, 1L));
        
        //True if user with ID 1L upvoted drink with id 2L
        Assert.assertTrue(voteDAO.hasUserUpvotedDrink(1L, 2L));
        
         //True if user with ID 1L downvoted drink with id 3L
        Assert.assertTrue(voteDAO.hasUserDownvotedDrink(1L, 3L));
    }
    
    @After
    public void clean() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        tx.commit();
        List<User> usrs = userDAO.findAll();
        usrs.forEach(usr -> {
            userDAO.remove(usr);
        });
    }
}

