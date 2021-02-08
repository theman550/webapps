package net.adrianh.drink.model.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"drink", "user_id"})
@ToString(exclude = {"drink","user_id"})

@AllArgsConstructor
public class Vote implements Serializable {
    
    @EmbeddedId
    private PK pk;
    
    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class PK implements Serializable {
        private Long user_id;
        private Long drink;
    }
    
    //a vote is cast by a user
    @MapsId("user_id")
    @ManyToOne(optional = false)
    @JoinColumn(name="user_id")
    private User user_id;
    
    //a vote is cast on a drink
    @MapsId("drink")
    @ManyToOne(optional = false)
    @JoinColumn(name="drink_id")
    private Drink drink;
    
    // a vote can be either positive or negative
    private int val;
    
}
