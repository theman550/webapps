/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.resources;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import net.adrianh.drink.authorization.Secured;
import net.adrianh.drink.model.dao.DrinkDAO;
import net.adrianh.drink.model.dao.UserDAO;
import net.adrianh.drink.model.dao.VoteDAO;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.User;
import net.adrianh.drink.model.entity.Vote;

/**
 *
 * @author andra
 */

@Path("votes")
public class VoteResource {
      
    @EJB
    DrinkDAO drinkDAO;
    @EJB
    UserDAO userDAO;
    @EJB
    VoteDAO voteDAO;
    
    
    
    @POST
    @Secured
    @Path("upvote")
    @Consumes(MediaType.APPLICATION_JSON)
    public int upvote(Drink d, @Context SecurityContext securityContext) {
         //Get the name of the authorized user (derived from a valid token)
       User authorizedUser = userDAO.findUserByName(securityContext.getUserPrincipal().getName()).get(0);
       long userID = authorizedUser.getId();
       long drinkID = d.getId();
       Drink drink = drinkDAO.findDrinkByID(drinkID);
       
            //upvote drink
       if(!voteDAO.hasUserVotedDrink(userID, drinkID)){
            Vote v = new Vote(null, authorizedUser, d, 1);          
            voteDAO.create(v);
                         
            //already upvoted? remove upvote
       } else if(voteDAO.hasUserUpvotedDrink(userID,drinkID)){
           voteDAO.remove(voteDAO.selectVote(userID, drinkID));

         //already downvoted? swap vote
       } else if(voteDAO.hasUserDownvotedDrink(userID,drinkID)){
            voteDAO.remove(voteDAO.selectVote(userID, drinkID));
            Vote v = new Vote(null, authorizedUser, d, 1);            
            voteDAO.create(v);                
       }
      
       drink.setVoteCount(drinkDAO.findAllDrinkVotes(drinkID));
       drinkDAO.update(drink);
       return drink.getVoteCount();
    }    
    
    
    @POST
    @Secured
    @Path("downvote")
    @Consumes(MediaType.APPLICATION_JSON)
    public int downvote(Drink d, @Context SecurityContext securityContext) {
         //Get the name of the authorized user (derived from a valid token)
       User authorizedUser = userDAO.findUserByName(securityContext.getUserPrincipal().getName()).get(0);
       long userID = authorizedUser.getId();
       long drinkID = d.getId();
       Drink drink = drinkDAO.findDrinkByID(drinkID);
       
            //downvote drink
       if(!voteDAO.hasUserVotedDrink(userID, drinkID)){
            Vote v = new Vote(null, authorizedUser, d, (-1));          
            voteDAO.create(v);
            
            //already downvoted? remove downvote
       } else if(voteDAO.hasUserDownvotedDrink(userID,drinkID)){
           voteDAO.remove(voteDAO.selectVote(userID, drinkID));
        
            //already upvoted? swap vote
       } else if(voteDAO.hasUserUpvotedDrink(userID,drinkID)){
            voteDAO.remove(voteDAO.selectVote(userID, drinkID));
            Vote v = new Vote(null, authorizedUser, d, (-1)); 
            voteDAO.create(v);
       }
       
       drink.setVoteCount(drinkDAO.findAllDrinkVotes(drinkID));
       drinkDAO.update(drink);  
       return drink.getVoteCount();
    }    
    
    
    
}
