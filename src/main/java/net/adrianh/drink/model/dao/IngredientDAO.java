package net.adrianh.drink.model.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.Ingredient;

@Stateless
public class IngredientDAO extends AbstractDAO<Ingredient> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;
    
    public IngredientDAO() {
        super(Ingredient.class);
    }
    
    public List<Drink> findIngredientsMatchingName() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
