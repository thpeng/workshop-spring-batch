package ch.thp.proto.ws.spring.batch.batch.company;

import ch.thp.proto.ws.spring.batch.company.domain.Address;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ch.thp.proto.ws.spring.batch.infrastructure.DatabaseConfig;
import javax.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;

/**
 *
 * @author thierry
 */

@Configuration
@Import({DatabaseConfig.class})
public class BatchCompanyImportConfiguration {

    @Autowired
    private StepBuilderFactory steps;
    
    @Autowired
    private JobBuilderFactory jobBuilders;
    
    @Bean
    @Autowired
	public Job flatfileToDbJob(@Qualifier("step1") Step step){
		return jobBuilders.get("flatfileToDbJob")
				.start(step)
				.build();
	}

    @Bean
    @Autowired
    @Qualifier("step1")
    public Step buildStep(JpaItemWriter writer, ItemReader<Address> addressItemReader) {
        return steps.get("importHqAddressStep")
                .<Address, Address>chunk(1)
                .reader(addressItemReader)
                .writer(writer)
                .build();
    }
    
    @Bean
    @Autowired
    public JpaItemWriter<Address> buildAddressItemWriter(EntityManagerFactory emf){
        JpaItemWriter<Address> writer =  new JpaItemWriter<>(); 
        writer.setEntityManagerFactory(emf);
        return writer; 
    }
    
    @Bean
    @Autowired
    @StepScope
    public FlatFileItemReader<Address> buildItemReader(LineMapper<Address> addressLineMapper, @Value("#{jobParameters[pathtoflatfile]}") String pathToFlatFile){
        FlatFileItemReader<Address> itemReader = new FlatFileItemReader<>(); 
        itemReader.setResource(new DefaultResourceLoader().getResource(pathToFlatFile));
        itemReader.setLineMapper(addressLineMapper);
        return itemReader; 
    }
    
    @Bean
    @StepScope
    public LineMapper<Address> buildLineMapper() {

        DefaultLineMapper<Address> mapper = new DefaultLineMapper<>();
        
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer("|");
        tokenizer.setNames(new String[]{"street", "numberOfStreet", "plz", "municipality"});
        tokenizer.setIncludedFields(new int[]{1, 2, 3, 4});

        BeanWrapperFieldSetMapper<Address> internalMapper = new BeanWrapperFieldSetMapper<>();
        internalMapper.setTargetType(Address.class);

        mapper.setFieldSetMapper(internalMapper);
        mapper.setLineTokenizer(tokenizer);
        return mapper; 
               
              
    }

}
