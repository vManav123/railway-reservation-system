package railway.reservation.system.configuration.batch.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import railway.reservation.system.model.controllerBody.Credentials;

@EnableBatchProcessing
@Configuration
@Slf4j
public class BatchProcessing {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Job readCSVFile() {
        log.info("Job reading Data from CSV File");
        return jobBuilderFactory
                .get("readCSVFile")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }
    @Bean
    public Step step1() {
        log.info("Step 1 Followed");
        return stepBuilderFactory
                .get("step1").<Credentials, Credentials>chunk(10)
                .reader(reader())
                .writer(writer()).build();
    }

    @Bean
    public FlatFileItemReader<Credentials> reader() {
        log.info("Data Reading...");
        FlatFileItemReader<Credentials> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("Credentials.csv"));
        reader.setLineMapper(new DefaultLineMapper<Credentials>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"username","password","user_id","roles"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(Credentials.class);
            }});
        }});
        return reader;
    }

    @Bean
    public MongoItemWriter<Credentials> writer() {
        log.info("Data Writing...");
        MongoItemWriter<Credentials> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("Credentials");
        return writer;
    }

}
