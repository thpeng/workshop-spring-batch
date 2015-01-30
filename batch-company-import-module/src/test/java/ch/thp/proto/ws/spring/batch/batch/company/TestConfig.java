/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.thp.proto.ws.spring.batch.batch.company;

import ch.thp.proto.ws.spring.batch.company.domain.Company;
import ch.thp.proto.ws.spring.batch.company.domain.CompanyRepository;
import ch.thp.proto.ws.spring.batch.infrastructure.DataLoader;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author thierry
 */
@Configuration
@EnableBatchProcessing
public class TestConfig {
    
    @Autowired
    private CompanyRepository cRepo; 
    
   @Bean
   public DataLoader loadCompanies(){
       return new DataLoader() {

           @Override
           public void load() {
              cRepo.save(new Company("ABCD-1234", "Firma A"));
              cRepo.save(new Company("DEFG-1234", "Firma A"));
           }
       }; 
   }
}
