package com.kiki.SpringBatchCsvtoCsv.config;

import com.kiki.SpringBatchCsvtoCsv.model.Employee;
import com.kiki.SpringBatchCsvtoCsv.processor.EmployeeProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
@EnableBatchProcessing
public class EmployeeBatchConfiguration
{
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public FlatFileItemReader<Employee> readDataFromCsv(){
        FlatFileItemReader<Employee> reader=new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("D:/study/data/Employee.csv"));
        reader.setLineMapper(new DefaultLineMapper<Employee>(){
            {
                setLineTokenizer(new DelimitedLineTokenizer(){
                    {
                        setNames(Employee.fields());
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<>(){
                    {
                        setTargetType(Employee.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public EmployeeProcessor processor()
    {
        return new EmployeeProcessor();
    }

    @Bean
    public FlatFileItemWriter<Employee> employeeWriter()
    {
        FlatFileItemWriter flatFileItemWriter=new FlatFileItemWriter();
        flatFileItemWriter.setResource(new FileSystemResource("D:/study/data/Employee_out.csv"));
        DelimitedLineAggregator<Employee> aggregator=new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Employee> fieldExtractor=new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(Employee.fields());
        aggregator.setFieldExtractor(fieldExtractor);
        flatFileItemWriter.setLineAggregator(aggregator);
        return flatFileItemWriter;
    }

    @Bean
    public Step executeEmployeeStep()
    {
        return stepBuilderFactory.get("executeEmployeeStep").<Employee,Employee>chunk(5)
                .reader(readDataFromCsv()).processor(processor()).writer(employeeWriter())
                .build();
    }

    @Bean
    public Job processEmployeeJob()
    {
        return jobBuilderFactory.get("processEmployeeJob").flow(executeEmployeeStep()).end().build();
    }
}
