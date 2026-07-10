package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.impl;


import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.UserDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements mapper<UserEntity, UserDto> {
    private ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    //converts entity to dto
    public UserDto mapto(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    //converts dto to Entity
    public UserEntity mapfrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }

}
