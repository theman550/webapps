package net.adrianh.drink.model.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.Vote;

@Stateless
public class VoteDAO extends AbstractDAO<Vote> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;

    public VoteDAO() {
        super(Vote.class);
    }
}