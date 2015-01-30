/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.thp.proto.ws.spring.batch.company.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author thierry
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{
    
    public Address findByStreetAndNumberOfStreetAndPlz(String street, String numberOfStreet, int plz);
    
}
