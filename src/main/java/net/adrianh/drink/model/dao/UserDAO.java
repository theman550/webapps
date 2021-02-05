/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.model.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.User;

/**
 *
 * @author Guy Fieri
 */
@Stateless
public class UserDAO extends AbstractDAO<User> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;

    public UserDAO() {
        super(User.class);
    }
}
