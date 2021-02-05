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
import net.adrianh.drink.model.entity.Vote;

/**
 *
 * @author Guy Fieri
 */
@Stateless
public class VoteDAO extends AbstractDAO<Vote> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;

    public VoteDAO() {
        super(Vote.class);
    }
}