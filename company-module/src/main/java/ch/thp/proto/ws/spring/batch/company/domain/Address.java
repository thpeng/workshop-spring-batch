/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.thp.proto.ws.spring.batch.company.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author thierry
 */
@Entity
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; 
    private String street; 
    private String numberOfStreet; 
    private int plz; 
    private String municipality; 

    public Address( String street, String numberOfStreet, int plz, String municipality) {
        this.street = street;
        this.numberOfStreet = numberOfStreet;
        this.plz = plz;
        this.municipality = municipality;
    }    
}
