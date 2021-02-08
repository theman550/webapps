package net.adrianh.drink.model.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.User;

@Stateless
public class UserDAO extends AbstractDAO<User> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;

    public UserDAO() {
        super(User.class);
    }
}
