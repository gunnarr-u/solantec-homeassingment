package com.solanteq.assingment;

import com.hazelcast.core.HazelcastInstance;
import com.solanteq.assingment.dataprocessors.AbstractDataHandler;
import com.solanteq.assingment.dataprocessors.DataExtractorImpl;
import com.solanteq.assingment.dataprocessors.F1F2DataProcessor;
import com.solanteq.assingment.dataprocessors.FileDataPublisher;
import com.solanteq.assingment.functions.BusinessFunction;
import com.solanteq.assingment.functions.impl.PlugableGroovyScritpFunction;
import com.solanteq.assingment.services.Broker;
import com.solanteq.assingment.services.ReaderService;
import com.solanteq.assingment.services.RecordGroupingService;
import com.solanteq.assingment.services.WriterService;
import com.solanteq.assingment.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
@Configuration
public class AppConfig {

    @Value("${input.file.path}")
    String inputPath;

    @Value("${output.file.path}")
    String outputPath;
    @Value("${f1.parameters.count}")
    private int groupSize;

    @Bean
    public ReaderService<int[]> readerService() {
        return new FileReaderService<>(new PathResource(inputPath),(line, lineNumber) -> {
            int[] ints = new int[2];
            String[] input = line.split(" ", 2);
            for (int i = 0; i < ints.length; i++) {
                ints[i] = Integer.parseInt(input[i]);
            }
            return ints;
        } );
    }

    @Bean
    public RecordGroupingService<int[]> groupingService(ReaderService<int[]> readerService) {
        return new RecordGroupingServiceImpl<>(readerService, groupSize);
    }

    @Bean
    public WriterService<Integer> fileWriterService() {
        return new FileWriterService<>(new PathResource(outputPath));
    }

    @Bean
    @Profile("singleNode")
    public Broker broker() {
        return new LocalBroker();
    }

    @Bean
    @Profile({"masterNode", "slaveNode"})
    public Broker distributedBroker(HazelcastInstance hazelcast) {
        return new HazelcastBroker(hazelcast);
    }

    @Bean
    @Profile({"masterNode", "singleNode"})
    public AbstractDataHandler dataExtractor(RecordGroupingService<int[]> recordGroupingService, Broker broker) {
        return new DataExtractorImpl(recordGroupingService, broker, 100);
    }

    @Bean
    @Profile("masterNode")
    public AbstractDataHandler fileDataPublisher(Broker broker, WriterService<Integer> fileWriterService) {
        FileDataPublisher fileDataPublisher = new FileDataPublisher(broker, fileWriterService);
        fileDataPublisher.setListenerCallback(this::exitApplication);
        return fileDataPublisher;
    }

    @Bean
    @Profile("masterNode")
    public AbstractDataHandler f1f2DataProcessor(Broker broker) {
        return new F1F2DataProcessor(broker, f1(), f2());
    }

    @Bean
    @Profile({"slaveNode", "!masterNode"})
    public AbstractDataHandler executionFinishAwaref1f2DataProcessor(Broker broker) {
        F1F2DataProcessor f1F2DataProcessor = new F1F2DataProcessor(broker, f1(), f2());
        f1F2DataProcessor.setListenerCallback(this::exitApplication);
        return f1F2DataProcessor;
    }

    @Bean
    public LifecycleProcessor lifecycleProcessor() {
        DefaultLifecycleProcessor lifecycleProcessor = new DefaultLifecycleProcessor();
        return lifecycleProcessor;
    }

    @Bean
    public BusinessFunction<Integer, Integer> f1() {
        return  new PlugableGroovyScritpFunction<>(new ResourceScriptSource(new ClassPathResource("f1.groovy")));
    }
    @Bean
    public BusinessFunction<Integer, Integer> f2() {
        return new PlugableGroovyScritpFunction<>(new ResourceScriptSource(new ClassPathResource("f2.groovy")));
    }

    @Autowired
    ApplicationContext applicationContext;


    public void exitApplication() {
        SpringApplication.exit(applicationContext, () -> 0);
    }

}
