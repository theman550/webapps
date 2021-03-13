/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.resources;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
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
    IngredientDAO ingredientDAO;
    @EJB
    UserDAO userDAO;
    @EJB
    VoteDAO voteDAO;

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    public Response upvote(Drink d, @Context SecurityContext securityContext) {
         //Get the name of the authorized user (derived from a valid token)
       User authorizedUser = userDAO.findUserByName(securityContext.getUserPrincipal().getName()).get(0);
       long userID = authorizedUser.getId();
       long drinkID = d.getId();
       
            //upvote drink
       if(!voteDAO.hasUserVotedDrink(userID, drinkID)){
            Vote v = new Vote(null, authorizedUser, d, 1);          
            voteDAO.create(v);
            return Response.status(Response.Status.OK).entity("added").build();
            
            //already upvoted? remove upvote
       } else if(voteDAO.hasUserUpvotedDrink(userID,drinkID)){
           voteDAO.remove(voteDAO.selectVote(userID, drinkID));
           return Response.status(Response.Status.OK).entity("removed").build();
       }
       
       return Response.status(Response.Status.CONFLICT).build(); 
    }    
}
