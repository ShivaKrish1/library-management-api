package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.impl;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.BooksDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BooksMapperImpl implements mapper<BooksEntity, BooksDto> {
    private ModelMapper modelMapper;

    public BooksMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    //Converts an entity to a DTO to safely send data from the API without exposing database details
    public BooksDto mapto(BooksEntity booksEntity) {
        return modelMapper.map(booksEntity, BooksDto.class);
    }

    @Override
    //Converts a DTO to an entity so API data can be processed and stored in the database
    public BooksEntity mapfrom(BooksDto booksDto) {
        return modelMapper.map(booksDto, BooksEntity.class);
    }

}
