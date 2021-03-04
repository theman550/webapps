package net.adrianh.drink.model.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.QUser;
import net.adrianh.drink.model.entity.User;

@Stateless
public class UserDAO extends AbstractDAO<User> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;

    public UserDAO() {
        super(User.class);
    }   
    
    public User login(String name, String pw){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser user = QUser.user;
        List<User> users = queryFactory.selectFrom(user)
            .where(user.name.eq(name).and(user.password.eq(pw)))
            .fetch();
        return users.get(0);
    }
    
    public boolean checkExist(String name, String pw){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser user = QUser.user;
        List<User> users = queryFactory.selectFrom(user)
            .where(user.name.eq(name).and(user.password.eq(pw)))
            .fetch();
        
        return users.size() > 0;
    }
    
}
