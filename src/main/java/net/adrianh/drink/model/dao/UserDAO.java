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
    
    public List<User> findUserByID(Long id){
	JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
	QUser user = QUser.user;
	List<User> users = queryFactory.selectFrom(user)
	    .where(user.id.eq(id))
	    .fetch();
	return users;
    }
}
