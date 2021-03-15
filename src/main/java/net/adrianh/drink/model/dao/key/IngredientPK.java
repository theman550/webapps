/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.model.dao.key;

import java.io.Serializable;
import lombok.Data;

@Data
public class IngredientPK implements Serializable {

    private Long drink;
    private String name;
}
