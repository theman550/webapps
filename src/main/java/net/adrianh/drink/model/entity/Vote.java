package net.adrianh.drink.model.entity;

import java.io.Serializable;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.adrianh.drink.model.dao.keyPackage.votePK;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"drink", "user_id"})
@ToString(exclude = {"drink", "user_id"})

@AllArgsConstructor
public class Vote implements Serializable {

    @EmbeddedId
    private votePK pk;

    //a vote is cast by a user
    @MapsId("user_id")
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonbTransient
    private User user_id;

    //a vote is cast on a drink
    @MapsId("drink")
    @ManyToOne(optional = false)
    @JoinColumn(name = "drink_id")
    @JsonbTransient
    private Drink drink;

    // a vote can be either positive or negative
    private int val;

}
