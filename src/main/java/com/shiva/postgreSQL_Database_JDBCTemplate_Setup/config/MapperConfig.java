package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
       /*Uses a more flexible matching strategy so ModelMapper can better map
       properties whose names don't exactly match, including some nested properties.*/
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;

    }
}
