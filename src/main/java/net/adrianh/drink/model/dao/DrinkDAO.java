package net.adrianh.drink.model.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.QDrink;

@Stateless
public class DrinkDAO extends AbstractDAO<Drink> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;

    public DrinkDAO() {
        super(Drink.class);
    }
    
    public List<Drink> findDrinksMatchingName(String s){
	JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
	QDrink drink = QDrink.drink;
	List<Drink> drinks = queryFactory.selectFrom(drink)
	    .where(drink.name.eq(s))
	    .fetch();
	return drinks;
    }
    public List<Drink> findDrinkByID(Long id){
	JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
	QDrink drink = QDrink.drink;
	List<Drink> drinks = queryFactory.selectFrom(drink)
	    .where(drink.id.eq(id))
	    .fetch();
	return drinks;
    }
    public List<Drink> findDrinksStartMatchingName(String s){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        List<Drink> drinks = queryFactory.selectFrom(drink)
            .where(drink.name.startsWithIgnoreCase(s))
            .limit(5)
            .orderBy(drink.voteCount.desc())
            .fetch();
        return drinks;
    }
    //find matching name from offset ordered by popularity
    public QueryResults<Drink> findDrinksMatchingNameFromOffset(String s, int offset) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
	QDrink drink = QDrink.drink;
	QueryResults<Drink> drinks = queryFactory.selectFrom(drink)
	    .where(drink.name.eq(s))
            .limit(20)
            .offset(offset)
            .orderBy(drink.voteCount.desc())
	    .fetchResults();
	return drinks;
    }
    public QueryResults<Drink> findMostPopularFromOffset(int offset) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        QueryResults<Drink> drinks = (QueryResults<Drink>) queryFactory.selectFrom(drink)
            .offset(offset)
            .limit(20)
            .orderBy(drink.voteCount.desc())
            .fetchResults();
        return drinks;
    }
    public QueryResults<Drink> findNewestFromOffset(int offset) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        QueryResults<Drink> drinks = (QueryResults<Drink>) queryFactory.selectFrom(drink)
            .offset(offset)
            .limit(20)
            .orderBy(drink.createdAt.desc())
            .fetchResults();
        return drinks;
    }
}
