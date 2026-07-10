package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.impl;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.AuthorDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.AuthorEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapperImpl implements mapper<AuthorEntity, AuthorDto> {
    private ModelMapper modelMapper;

    public AuthorMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    //Converts an entity to a DTO to safely send data from the API without exposing database details
    public AuthorDto mapto(AuthorEntity authorEntity) {
        return modelMapper.map(authorEntity, AuthorDto.class);
    }

    @Override
    //Converts a DTO to an entity so API data can be processed and stored in the database
    public AuthorEntity mapfrom(AuthorDto authorDto) {
        return modelMapper.map(authorDto, AuthorEntity.class);
    }

}
