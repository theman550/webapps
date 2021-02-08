package net.adrianh.drink.model.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.Ingredient;
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
}