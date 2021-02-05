package net.adrianh.drink.model.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {
    
    
    public enum Unit {
        MILLILITRE, CENTILITRE, DECILITRE, LITRE, GRAMS, PIECES 
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // TODO: Fix composite keys
    private String name; 
    @Enumerated(EnumType.ORDINAL)
    private Unit unit;
    private double amount;
    
    //alcohol percentage
    private double abv;
    
    //ingredient belongs to drink as element of list
    @ManyToOne(optional = false, cascade=CascadeType.REMOVE)
    @JoinColumn(name="drink_id")
    private Drink drink;
}
