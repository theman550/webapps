package net.adrianh.drink.model.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.Ingredient;
import net.adrianh.drink.model.entity.QDrink;
import net.adrianh.drink.model.entity.QIngredient;

@Stateless
public class DrinkDAO extends AbstractDAO<Drink> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;

    public DrinkDAO() {
        super(Drink.class);
    }
    
    public List<Drink> allDrinks(){
	JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
	QDrink drink = QDrink.drink;
	List<Drink> drinks = queryFactory.selectFrom(drink)
	    .fetch();
        
	QIngredient ingredient = QIngredient.ingredient;
	List<Ingredient> ingredients = queryFactory.selectFrom(ingredient)
	    .fetch();
      
        for(Drink d : drinks){
            for(Ingredient i : ingredients){
                if(Objects.equals(d.getId(), i.getDrink())){
                    d.addIngredient(i);   
                }
            }
        }

	return drinks;
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
}
