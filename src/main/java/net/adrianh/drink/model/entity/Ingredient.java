package net.adrianh.drink.model.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.json.bind.annotation.JsonbTransient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
class IngredientPK implements Serializable {
    private Long drink;
    private String name;
}

@Data
@Entity
@IdClass(IngredientPK.class)
@EqualsAndHashCode(exclude = "drink")
@ToString(exclude = "drink")
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {
    
    
    public enum Unit {
        MILLILITRE, CENTILITRE, DECILITRE, LITRE, GRAMS, PIECES 
    }
    
    @Id
    private String name; 
    
    @Enumerated(EnumType.STRING)
    private Unit unit;
    private double amount;
    
    //alcohol percentage
    private double abv;
    
    //ingredient belongs to drink as element of list
    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonbTransient
    @JoinColumn(name="drink_id")
    private Drink drink;
}
