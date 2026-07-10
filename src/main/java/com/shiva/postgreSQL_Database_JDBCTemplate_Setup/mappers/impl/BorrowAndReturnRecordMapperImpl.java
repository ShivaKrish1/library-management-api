package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.impl;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.BorrowAndReturnRecordDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BorrowAndReturnRecordEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BorrowAndReturnRecordMapperImpl implements mapper<BorrowAndReturnRecordEntity, BorrowAndReturnRecordDto> {
    private ModelMapper modelMapper;

    public BorrowAndReturnRecordMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    //Converts an entity to a DTO to safely send data from the API without exposing database details
    public BorrowAndReturnRecordDto mapto(BorrowAndReturnRecordEntity borrowRecordEntity) {
        return modelMapper.map(borrowRecordEntity, BorrowAndReturnRecordDto.class);
    }

    @Override
    //Converts a DTO to an entity so API data can be processed and stored in the database
    public BorrowAndReturnRecordEntity mapfrom(BorrowAndReturnRecordDto borrowRecordDto) {
        return modelMapper.map(borrowRecordDto, BorrowAndReturnRecordEntity.class);
    }

}
