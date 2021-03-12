package net.adrianh.drink.model.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            .where(user.accountName.eq(name).and(user.password.eq(pw)))
            .fetch();
        return users.get(0);
    }
    

    public boolean areCredentialsMatching(String name, String pw){        
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser user = QUser.user;
        List<User> users = queryFactory.selectFrom(user)
            .where(user.accountName.eq(name).and(user.password.eq(pw)))
            .fetch();
                
        return !users.isEmpty();
    }
    
    public boolean isAccNameUnique(String name){        
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser user = QUser.user;
        List<User> users = queryFactory.selectFrom(user)
            .where(user.accountName.eq(name))
            .fetch();
        
        return users.isEmpty();
    }
    

    public List<User> findUserByID(Long id){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser user = QUser.user;
        List<User> users = queryFactory.selectFrom(user)
            .where(user.id.eq(id))
            .fetch();
        return users;
    }
  
   public String findSaltByName(String name){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser user = QUser.user;
        List<User> users = queryFactory.selectFrom(user)
          .where(user.accountName.eq(name))
          .fetch();

      return users.get(0).getSalt();
    }
  
    public List<User> findUserByName(String name) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser user = QUser.user;
        List<User> users = queryFactory.selectFrom(user)
                .where(user.accountName.eq(name))
                .fetch();
        return users;
    }
}
