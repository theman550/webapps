/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.resources;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.adrianh.drink.Secured;
import net.adrianh.drink.model.dao.DrinkDAO;
import net.adrianh.drink.model.dao.IngredientDAO;
import net.adrianh.drink.model.dao.UserDAO;
import net.adrianh.drink.model.dao.VoteDAO;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.Ingredient;
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
    public Response upvote(Drink d, @Context SecurityContext securityContext) {
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
      
       drink.setVoteCount(drinkDAO.updateVoteCount(drinkID));
       drinkDAO.update(drink);
       return Response.status(Response.Status.OK).entity(drink.getVoteCount()).build();
    }    
    
    
    @POST
    @Secured
    @Path("downvote")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response downvote(Drink d, @Context SecurityContext securityContext) {
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
       
       drink.setVoteCount(drinkDAO.updateVoteCount(drinkID));
       drinkDAO.update(drink);
       return Response.status(Response.Status.OK).entity(drink.getVoteCount()).build();     
    }    
    
    
    
}
