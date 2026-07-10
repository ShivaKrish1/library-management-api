package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.controllers;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.UserDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.mapper;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class UserController {
    private UserService userService;
    private mapper<UserEntity, UserDto> mapper;

    public UserController(UserService userService, mapper<UserEntity, UserDto> mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserEntity userEntity = mapper.mapfrom(userDto);
        UserEntity result = userService.createUser(userEntity);
        UserDto userDto1 = mapper.mapto(result);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public Page<UserDto> listUsers(Pageable pageable) {
        Page<UserEntity> userEntities = userService.findAllUsers(pageable);
        return userEntities.map(mapper::mapto);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        Optional<UserEntity> foundUser = userService.findOneUser(id);
        //what this .map function does it turns the Optional<UserEntity> into just an UserEntity Object
        //so it can be mapped to a DTO, the rzn it is optional to begin with is because for findOneUser
        //method we use find by id given function which is of type Optional
        return foundUser.map(userEntity -> {
            //if it does exist
            UserDto userDto = mapper.mapto(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }

    @PutMapping("/users/{id}")
    //updating the id with new user contents
    public ResponseEntity<UserDto> fullUpdateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) {
        if (!userService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserEntity userEntity = mapper.mapfrom(userDto);
        UserEntity result = userService.fullUpdateUser(id, userEntity);
        UserDto dtoResult = mapper.mapto(result);
        return new ResponseEntity<>(dtoResult, HttpStatus.OK);


    }

    @PatchMapping("users/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        if (!userService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserEntity userEntity = mapper.mapfrom(userDto);
        UserEntity result = userService.partialUpdateUser(id, userEntity);
        UserDto dto = mapper.mapto(result);
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        if (!userService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}
