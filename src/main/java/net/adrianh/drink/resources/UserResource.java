/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.resources;

import io.jsonwebtoken.Jwts;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.Random;
import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
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
       
       if(userDAO.checkExist(name, pw)){
            User user = userDAO.login(name, pw);
            
            // Generate signed
            String jws = createToken(user.getName());

            return Response.status(Response.Status.OK).entity(Json.createObjectBuilder().add("token", jws).add("username",user.getName()).build()).build();  
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
        user.setSalt(generateMockSalt());
        user.setPassword(pw+user.getSalt());
        userDAO.create(user);
        return Response.status(Response.Status.OK).entity("User created!").build();  
    }  
    
    private String generateMockSalt(){
        byte[] array = new byte[7]; 
        new Random().nextBytes(array);
        String mockSalt = new String(array, Charset.forName("UTF-8"));

        return mockSalt;
    }
}
