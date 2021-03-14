package net.adrianh.drink.model.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.Ingredient;
import net.adrianh.drink.model.entity.QDrink;
import net.adrianh.drink.model.entity.QIngredient;

@Stateless
public class IngredientDAO extends AbstractDAO<Ingredient> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;
    
    public IngredientDAO() {
        super(Ingredient.class);
    }
    
    public List<Ingredient> findIngredientsMatchingName(String s) {
	JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
	QIngredient ingredient = QIngredient.ingredient;
	List<Ingredient> ingredients = queryFactory.selectFrom(ingredient)
	    .where(ingredient.name.eq(s))
	    .fetch();
	return ingredients;
    }

    public List<Drink> findDrinksFromIngredient(String i){
	List<Ingredient> ingredients = findIngredientsMatchingName(i);
	List<Drink> drinkar = new ArrayList<Drink>();
	for(Ingredient ing: ingredients){
	    drinkar.add(ing.getDrink());
	}
	return drinkar;
    }
    
    public List<Ingredient> findIngredientsStartingWith(String s){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QIngredient ingredient = QIngredient.ingredient;
        List<Ingredient> ingredients = queryFactory.selectFrom(ingredient)
            .where(ingredient.name.startsWithIgnoreCase(s))
            .limit(5)
            .orderBy(ingredient.drink.voteCount.desc())
            .fetch();
        return ingredients;
    }
    //find matching name from offset ordered by popular
    public QueryResults<Drink> findDrinksFromIngredientsMatchingNameFromOffset(String s, int offset, String user) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
	QDrink drink = QDrink.drink;
        JPAQuery jpaQuery = queryFactory.selectFrom(drink)
            .where(drink.ingredients.any().name.eq(s))
            .limit(20)
            .offset(offset)
            .orderBy(drink.voteCount.desc());
        if (user != null) {
            jpaQuery.where(drink.user.accountName.eq(user));
        }
	QueryResults<Drink> drinks = (QueryResults<Drink>) jpaQuery.fetchResults();
	return drinks;
    }
    //find matching name from offset ordered by newest
    public QueryResults<Drink> findDrinksFromIngredientsMatchingNameFromOffsetByNewest(String s, int offset, String user) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
	QDrink drink = QDrink.drink;
        JPAQuery jpaQuery = queryFactory.selectFrom(drink)
            .where(drink.ingredients.any().name.eq(s))
            .limit(20)
            .offset(offset)
            .orderBy(drink.createdAt.desc());
        if (user != null) {
            jpaQuery.where(drink.user.accountName.eq(user));
        }
	QueryResults<Drink> drinks = (QueryResults<Drink>) jpaQuery.fetchResults();
	return drinks;
    }
}