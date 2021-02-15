package net.adrianh.drink.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "DRINK_USER") // "USER" is reserved
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    //a user has many created drinks
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Drink> createdDrinks = new ArrayList<>();
    
    //a user has many votes
    @OneToMany(mappedBy="user_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();
    
    public void addVote(Vote vote) {
        votes.add(vote);
        vote.setUser_id(this);
    }
    
    public void addDrink(Drink drink) {
        createdDrinks.add(drink);
        drink.setUser(this);
    }
    
}