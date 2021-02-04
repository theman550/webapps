package net.adrianh.drink.model.dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.Getter;
import net.adrianh.drink.model.entity.Drink;

@Stateless
public class DrinkDAO extends AbstractDAO<Drink> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;
    
    public DrinkDAO() {
        super(Drink.class);
    }
    
    public List<Drink> findDrinksMatchingName(String s) {
        
        /*
        List<Drink> drinkList = super.findAll();
        List<Drink> returnList = new ArrayList<>();
        
        for (Drink drink : drinkList) { 
            if (drink.getName().equals(s)) {
                returnList.add(drink);
            }
        }
        return returnList;
        */
        
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        AbstractQuery<Drink> q = cb.createQuery(Drink.class);
        Root<Drink> c = q.from(Drink.class);
        q.distinct(true);
        q.where(cb.like(c.get("name"),s));
        CriteriaQuery<Drink> select = ((CriteriaQuery<Drink>) q).select(c);
        TypedQuery<Drink> tq = entityManager.createQuery(select);
        List<Drink> resultat = tq.getResultList();
        return resultat;
        
    }
}
