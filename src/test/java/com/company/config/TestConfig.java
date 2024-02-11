package com.company.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean(name = "testModelMapper")
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
