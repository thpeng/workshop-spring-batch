/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.thp.proto.ws.spring.batch.company.domain;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author thierry
 */
@Entity
@Data
@NoArgsConstructor
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; 
    private String companyId; 
    private String name; 
    @OneToOne
    private Address headquarter;
    @OneToMany
    private Set<Address>subsidiaries; 

    public Company(String companyId, String name) {
        this.companyId = companyId;
        this.name = name;
    }
    
    
    
     
    
}
