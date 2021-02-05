package net.adrianh.drink.model.entity;

import java.io.Serializable;
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

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Drink implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    
    //all the drinks votes
    @OneToMany(mappedBy="drink")
    private List<Vote> votes;
    
    //all the drinks ingredients
    @OneToMany(mappedBy = "drink", cascade=CascadeType.ALL)
    private List<Ingredient> ingredients;
    
    //drink belongs to user as liked element
    @ManyToOne(optional = false, cascade=CascadeType.REMOVE)
    @JoinColumn(name="user_id")
    private User user;
}
