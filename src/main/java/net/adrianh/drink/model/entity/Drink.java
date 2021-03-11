package net.adrianh.drink.model.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
  
    //all the drinks votes
    @OneToMany(mappedBy = "drink", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonbTransient
    private List<Vote> votes;

    //all the drinks ingredients
    @OneToMany(mappedBy = "drink", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients;
    private int voteCount;


    @PrePersist
    private void prePersist() {
        createdAt = new Date();
    }
    
    @PostLoad
    private void voteCount() { //TODO change to a query that fixes the counts of the votes
        voteCount = 0;
        for (Vote v : this.votes) {
            this.voteCount += v.getVal();
        }
    }

    //drink belongs to user as liked element
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public void addIngredient(Ingredient ingredientToAdd) {
        ingredients.add(ingredientToAdd);
        ingredientToAdd.setDrink(this);
    }
}
