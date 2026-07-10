package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.Controllers;


import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.AuthorDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.AuthorEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//THESE TESTS ARE FOR PRESENTATION LAYER/CONTROLLERS
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private AuthorService authorService;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorService = authorService;
    }

    @Test
    //testing if createAuthor Works
    public void createAuthorSuccessfullyReturns201() throws Exception {
        //create author entity
        AuthorEntity authorEntity = AuthorEntity.builder().id(null).name("Shiva").age(19).build();
        //convert as a json object
        String authorjson = objectMapper.writeValueAsString(authorEntity);
        // Perform a POST request to the /authors endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/authors")
                        //Specify that the request body is JSON
                        .contentType(MediaType.APPLICATION_JSON)
                        //add in the json representation
                        .content(authorjson))
                //verify that the controller returns HTTP 201 which is the sign that is created
                .andExpect(status().isCreated()
                );
    }

    @Test
    //testing if createAuthor Works
    public void createAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        //create author entity
        AuthorEntity authorEntity = AuthorEntity.builder().id(null).name("Shiva").age(19).build();
        //convert as a json object
        String authorjson = objectMapper.writeValueAsString(authorEntity);
        // Perform a POST request to the /authors endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/authors")
                        //Specify that the request body is JSON
                        .contentType(MediaType.APPLICATION_JSON)
                        //add in the json representation
                        .content(authorjson))
                //returns the contents of the authorEntity we made
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Shiva"));
    }

    @Test
    //testing if allAuthors returns 200
    public void allAuthorsSuccessfullyReturns200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authors"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    //testing if allAuthors Works
    public void allAuthorSuccessFullyReturnsSavedAuthor() throws Exception {
        AuthorEntity authorEntity1 = AuthorEntity.builder().id(null).name("Shiva").age(19).build();
        AuthorEntity authorEntity2 = AuthorEntity.builder().id(null).name("Bob").age(29).build();
        AuthorEntity authorEntity3 = AuthorEntity.builder().id(null).name("Jeff").age(40).build();
        authorService.createAuthor(authorEntity1);
        authorService.createAuthor(authorEntity2);
        authorService.createAuthor(authorEntity3);

        // Perform a POST request to the /authors endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/authors")
                        //Specify that the request body is JSON
                        .contentType(MediaType.APPLICATION_JSON))
                //returns the contents of the authorEntity List we made
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].age").value(19))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Shiva"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].age").value(29))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].age").value(40))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].name").value("Jeff"));
    }

    @Test
    //testing if correct HTTP is returned
    public void returnAuthorByIDSuccessfulHTTP() throws Exception {
        AuthorEntity authorEntity1 = AuthorEntity.builder().name("Shiva").age(19).build();
        authorService.createAuthor(authorEntity1);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/{id}", authorEntity1.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void allAuthorSuccessFullyReturnsGetAuthor() throws Exception {
        AuthorEntity authorEntity1 = AuthorEntity.builder().name("Shiva").age(19).build();
        authorService.createAuthor(authorEntity1);
        // Perform a POST request to the /authors endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/{id}", authorEntity1.getId())
                        //Specify that the request body is JSON
                        .contentType(MediaType.APPLICATION_JSON))
                //returns the contents of the authorEntity we made
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(19))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Shiva"));
    }

    @Test
    //testing if correct HTTP is returned
    public void UpdateAuthorByIdSuccessfulHTTP() throws Exception {
        AuthorEntity authorEntity = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        AuthorEntity savedAuthor = authorService.createAuthor(authorEntity);
        AuthorDto updatedAuthor = AuthorDto.builder()
                .name("Bob")
                .age(25)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/authors/{id}", savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isOk());
    }

    @Test
    //testing if correct content is returned
    public void UpdateAuthorByIdSuccessFulContent() throws Exception {
        AuthorEntity authorEntity = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        AuthorEntity savedAuthor = authorService.createAuthor(authorEntity);
        AuthorDto updatedAuthor = AuthorDto.builder()
                .name("Bob")
                .age(25)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/authors/{id}", savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(25));
    }

    @Test
    //testing if partial update returns correct HTTP
    public void partialUpdateAuthorCorrectHttp() throws Exception {
        AuthorEntity authorEntity = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        AuthorEntity savedAuthor = authorService.createAuthor(authorEntity);
        AuthorDto updatedAuthor = AuthorDto.builder()
                .age(25)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.patch("/authors/{id}", savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isOk());


    }

    @Test
    //testing if partial update returns the correct content
    public void partialUpdateAuthorCorrectContent() throws Exception {
        AuthorEntity authorEntity = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        AuthorEntity savedAuthor = authorService.createAuthor(authorEntity);
        AuthorDto updatedAuthor = AuthorDto.builder()
                .age(25)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.patch("/authors/{id}", savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Shiva"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(25));
    }

    @Test
    //testing if delete author returns correct HTTP
    public void deleteAuthorCorrectHTTP() throws Exception {
        AuthorEntity authorEntity = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        AuthorEntity savedAuthor = authorService.createAuthor(authorEntity);
        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/{id}", savedAuthor.getId()))
                .andExpect(status().isNoContent());
    }


}
