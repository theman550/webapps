/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adrianh.drink.resources;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author oussama
 */
@Data
@XmlRootElement
@AllArgsConstructor
public class AutoCompleteResponse {

    private String type;
    private String name;
    private int offset;

    public AutoCompleteResponse(String type, String name) {
        this.type = type;
        this.name = name;

    }
}
