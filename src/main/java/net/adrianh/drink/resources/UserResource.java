/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.resources;

import java.nio.charset.Charset;
import java.util.Random;
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
       
       if(userDAO.areCredentialsMatching(name, pw)){
            User user = userDAO.login(name, pw);
            return Response.status(Response.Status.OK).entity(user).build();  
       } else{
            return Response.status(Response.Status.UNAUTHORIZED).entity("No such user.").build();  
       } 
    }  
      
    @POST 
    @Path("create/{accName}/{dispName}/{pw}")
    public Response addUser(@PathParam("accName") String accName, 
                       @PathParam("dispName") String dispName,
                       @PathParam("pw") String pw){
        
        if(userDAO.isAccNameUnique(accName)){
            User user = new User();
            user.setAccountName(accName);
            user.setDisplayName(dispName);
            user.setSalt(generateMockSalt());
            user.setPassword(pw+user.getSalt());
            userDAO.create(user);
            return Response.status(Response.Status.OK).entity("User created!").build(); 
        } else{
           return Response.status(Response.Status.CONFLICT).entity("Account name not unique!").build(); 
        }
  
    }  
    
    private String generateMockSalt(){
        byte[] array = new byte[7]; 
        new Random().nextBytes(array);
        String mockSalt = new String(array, Charset.forName("UTF-8"));

        return mockSalt;
    }
}
