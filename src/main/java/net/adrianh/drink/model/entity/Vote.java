/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Vote {
    //a vote is cast by a user
    @Id 
    @ManyToOne(optional = false)
    private User user_id;
    //a drink has multiple votes, list of all votes by drink_id
    @Id
    @ManyToOne(optional = false, cascade=CascadeType.REMOVE)
    @JoinColumn(name="drink_id")
    private Drink drink;
    private int value;
    
}
