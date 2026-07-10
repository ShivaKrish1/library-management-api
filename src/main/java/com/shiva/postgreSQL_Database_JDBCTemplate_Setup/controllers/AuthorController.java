package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.controllers;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.AuthorDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.AuthorEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.mapper;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class AuthorController {

    private AuthorService authorService;
    private mapper<AuthorEntity, AuthorDto> mapper;

    public AuthorController(AuthorService authorService, mapper<AuthorEntity, AuthorDto> mapper) {
        this.authorService = authorService;
        this.mapper = mapper;
    }

    //method creates an author
    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        /*Converts the incoming AuthorDto from the request body
        into an AuthorEntity so it can be used by the service
        and persistence layers*/
        AuthorEntity authorEntity = mapper.mapfrom(author);
        /*Passes the AuthorEntity to the service layer, which
        performs any business logic and persists the entity
        to the database. The saved entity is returned, typically
        containing database-generated values such as the id.*/
        AuthorEntity savedAuthorEntity = authorService.createAuthor(authorEntity);
        /*Converts the saved AuthorEntity back into an AuthorDto
        and returns it as the HTTP response body*/
        return new ResponseEntity<>(mapper.mapto(savedAuthorEntity), HttpStatus.CREATED);
    }

    //this gets author with specific id
    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> returnAuthorById(@PathVariable("id") Long id) {
        Optional<AuthorEntity> foundAuthor = authorService.returnAuthorById(id);
        //what this .map function does it turns the Optional<AuthorEntity> into just an AuthorEntity Object
        //so it can be mapped to a DTO, the rzn it is optional to begin with is because for returnAuthorById
        //method we use find by id given function which is of type Optional
        return foundAuthor.map(authorEntity -> {
            //if it does exist
            AuthorDto authorDto = mapper.mapto(authorEntity);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    //this gets all authors
    @GetMapping("/authors")
    public Page<AuthorDto> listAuthors(Pageable pageable) {
        Page<AuthorEntity> authorEntityList = authorService.findAll(pageable);
        return authorEntityList.map(mapper::mapto);
    }

    //this updates the authors name and age
    @PutMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthorById(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto) {
        //cant update an author if it doesn't exist
        if (!authorService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AuthorEntity authorEntity = mapper.mapfrom(authorDto);
        AuthorEntity updatedAuthor = authorService.updateAuthor(id, authorEntity);
        AuthorDto savedAuthor = mapper.mapto(updatedAuthor);
        return new ResponseEntity<>(savedAuthor, HttpStatus.OK);
    }

    //this partially updates the author
    @PatchMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto) {
        if (!authorService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AuthorEntity authorEntity = mapper.mapfrom(authorDto);
        AuthorEntity savedAuthorEntity = authorService.partialUpdateAuthor(id, authorEntity);
        AuthorDto result = mapper.mapto(savedAuthorEntity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //deletes author from database
    @DeleteMapping("/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") Long id) {
        if (!authorService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
