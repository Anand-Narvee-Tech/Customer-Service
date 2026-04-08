package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // ✅ Register module to support Java 8 Date/Time types
        mapper.registerModule(new JavaTimeModule());
        // ✅ Disable timestamps so dates are serialized as 'yyyy-MM-dd' instead of numbers
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // ✅ Optional: fail on unknown properties false, to avoid deserialization issues
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}