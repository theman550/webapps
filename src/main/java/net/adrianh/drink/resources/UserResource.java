/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.resources;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import net.adrianh.drink.model.dao.UserDAO;
import net.adrianh.drink.model.entity.User;


/**
 * @author andra
 */

@Path("user")
public class UserResource {
   
    @EJB
    private UserDAO userDAO;
      
    @GET 
    @Path("login/{name}/{pw}")
    public Response loginUser(@PathParam("name") String name, 
                       @PathParam("pw") String pw){
       
       if(userDAO.checkExist(name, pw)){
            User user = userDAO.login(name, pw);
            return Response.status(Response.Status.OK).entity(user).build();  
       } else{
            return Response.status(Response.Status.UNAUTHORIZED).entity("No such user.").build();  
       } 
    }  
      
    @POST 
    @Path("create/{name}/{pw}")
    public Response addUser(@PathParam("name") String name, 
                       @PathParam("pw") String pw){
        
        User user = new User();
        user.setName(name);
        user.setPassword(pw);
        userDAO.create(user);
        return Response.status(Response.Status.OK).entity("User created!").build();  
    }  
}
