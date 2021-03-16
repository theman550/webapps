/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.resources;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.adrianh.drink.model.entity.Drink;

/**
 *
 * @author oussama
 */
@Data
@XmlRootElement
@AllArgsConstructor
public class DrinkResponse {

    private int total;
    private List<Drink> drinks;
}
