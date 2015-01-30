package ch.thp.proto.ws.spring.batch.batch.company;

import ch.thp.proto.ws.spring.batch.company.domain.AddressRepository;
import ch.thp.proto.ws.spring.batch.company.domain.Company;
import ch.thp.proto.ws.spring.batch.company.domain.CompanyRepository;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author thierry
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class,BatchCompanyImportConfiguration.class})
public class BatchCompanyImportTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private AddressRepository addressRepository;
    
     @Autowired
    private CompanyRepository companyRepository;

    @Before
    public void tearDown() {
        addressRepository.deleteAll();
    }

    @Test
    public void testImportHqSimple() throws Exception {

        Map<String, JobParameter> jobParametersMap = new HashMap<>();
        jobParametersMap.put("now", new JobParameter(LocalTime.now().toString()));
        jobParametersMap.put("pathtoflatfile", new JobParameter("classpath:import-hq-simple.txt"));

        // launch the job
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParametersMap));

        // assert job run status
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assert.assertEquals(2, addressRepository.count());
        Assert.assertNotNull(addressRepository.findByStreetAndNumberOfStreetAndPlz("Bernstrasse", "23", 3007));
        Assert.assertNotNull(addressRepository.findByStreetAndNumberOfStreetAndPlz("Oltenstrasse", "8a", 6000));

    }

    @Test
    public void testImportWithOnlyKnownCompanies() throws Exception {
        Map<String, JobParameter> jobParametersMap = new HashMap<>();
        jobParametersMap.put("now", new JobParameter(LocalTime.now().toString()));
        jobParametersMap.put("pathtoflatfile", new JobParameter("classpath:import-hq-with-unknown-company-id.txt"));

        // launch the job
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParametersMap));

        // assert job run status
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assert.assertEquals(2, addressRepository.count());
        
        Company companyA = companyRepository.findByCompanyId("ABCD-1234"); 
        Assert.assertEquals("Bernstrasse", companyA.getHeadquarter().getStreet());
        Assert.assertEquals(3007, companyA.getHeadquarter().getPlz());
        Assert.assertEquals("23", companyA.getHeadquarter().getNumberOfStreet());
        
        Company companyB = companyRepository.findByCompanyId("DEFG-1234"); 
        Assert.assertEquals("Oltenstrasse", companyB.getHeadquarter().getStreet());
        Assert.assertEquals(6000, companyB.getHeadquarter().getPlz());
        Assert.assertEquals("8a", companyB.getHeadquarter().getNumberOfStreet());
        
        Assert.assertNotNull(addressRepository.findByStreetAndNumberOfStreetAndPlz("Bernstrasse", "23", 3007));
        Assert.assertNotNull(addressRepository.findByStreetAndNumberOfStreetAndPlz("Oltenstrasse", "8a", 6000));
        

    }

    @Test
    public void testImportWithHeaderFooter() throws Exception {
        Map<String, JobParameter> jobParametersMap = new HashMap<>();
        jobParametersMap.put("now", new JobParameter(LocalTime.now().toString()));
        jobParametersMap.put("pathtoflatfile", new JobParameter("classpath:import-hq-with-header-footer.txt"));

        // launch the job
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParametersMap));

        // assert job run status
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        
        Company companyA = companyRepository.findByCompanyId("ABCD-1234"); 
        Assert.assertEquals("Bernstrasse", companyA.getHeadquarter().getStreet());
        Assert.assertEquals(3007, companyA.getHeadquarter().getPlz());
        Assert.assertEquals("23", companyA.getHeadquarter().getNumberOfStreet());
        
        Company companyB = companyRepository.findByCompanyId("DEFG-1234"); 
        Assert.assertEquals("Oltenstrasse", companyB.getHeadquarter().getStreet());
        Assert.assertEquals(6000, companyB.getHeadquarter().getPlz());
        Assert.assertEquals("8a", companyB.getHeadquarter().getNumberOfStreet());
        
        Assert.assertEquals(2, addressRepository.count());
        Assert.assertNotNull(addressRepository.findByStreetAndNumberOfStreetAndPlz("Bernstrasse", "23", 3007));
        Assert.assertNotNull(addressRepository.findByStreetAndNumberOfStreetAndPlz("Oltenstrasse", "8a", 6000));

    }

}
