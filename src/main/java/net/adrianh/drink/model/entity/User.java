/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.model.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Guy Fieri
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    private String name;
    //a user has many created drinks
    @OneToMany(mappedBy="user")
    private List<Drink> created_drinks;
    //a user has many votes
    @OneToMany(mappedBy="user_id")
    private List<Vote> votes;
    
}
