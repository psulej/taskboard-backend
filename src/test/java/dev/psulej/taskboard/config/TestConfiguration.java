package dev.psulej.taskboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }
}
