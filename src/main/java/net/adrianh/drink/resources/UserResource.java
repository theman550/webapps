/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.resources;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.json.Json;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import static net.adrianh.drink.TokenServices.createToken;

import net.adrianh.drink.model.dao.UserDAO;
import net.adrianh.drink.model.entity.User;


/**
 * @author andra
 */

@Path("user")
public class UserResource {
    
    @EJB
    private UserDAO userDAO;
      
    @POST 
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(@FormParam("name") String name, 
                       @FormParam("pw") String pw){
       
        String salt = userDAO.findSaltByName(name);
       
       if(userDAO.areCredentialsMatching(name, mockHash(pw+salt))){
            User user = userDAO.login(name, mockHash(pw+salt));
            // Generate signed
            String jws = createToken(user.getAccountName());

            return Response.status(Response.Status.OK).entity(Json.createObjectBuilder().add("token", jws).add("username",user.getAccountName()).build()).build();  
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
            user.setPassword(mockHash(pw+user.getSalt()));
            userDAO.create(user);
            return Response.status(Response.Status.OK).entity("User created!").build(); 
        } else{
           return Response.status(Response.Status.CONFLICT).entity("Account name not unique!").build(); 
        }
  
    }  
    
    private String mockHash(String password){
        
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        
        return new String(hashedPassword, Charset.forName("UTF-8"));     
    }
    
    private String generateMockSalt(){
        byte[] array = new byte[7]; 
        new Random().nextBytes(array);
       
        return new String(array, Charset.forName("UTF-8"));
    }
}
