package net.adrianh.drink.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.json.bind.annotation.JsonbTransient;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
@NoArgsConstructor
@AllArgsConstructor
public class Drink implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    private String name;
    private String description;
    
    //all the drinks votes
    @OneToMany(mappedBy="drink", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonbTransient
    private List<Vote> votes = new ArrayList<>();
    
    //all the drinks ingredients
    @OneToMany(mappedBy = "drink", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonbTransient
    private List<Ingredient> ingredients = new ArrayList<>();
    
    //drink belongs to user as liked element
    @ManyToOne(optional = false)
    @JoinColumn(name="user_id")
    @JsonbTransient
    private User user;
    
    public void addIngredient(Ingredient ingredientToAdd) {
        ingredients.add(ingredientToAdd);
        ingredientToAdd.setDrink(this);   
    }
}
