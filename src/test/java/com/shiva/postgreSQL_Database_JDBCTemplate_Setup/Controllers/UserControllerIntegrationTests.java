package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.Controllers;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.UserDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories.UserRepository;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.UserService;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, UserService userService, UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // ---------- happy paths ----------

    @Test
    public void checkIfCreateUserWorks() throws Exception {
        UserDto userDto = UserDto.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Shiva Krish"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("shivakrish727@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());
    }

    @Test
    public void checkIfListUsers() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        UserEntity userEntity2 = UserEntity.builder().name("Bob Myers").email("bob@gmail.com").build();
        userService.createUser(userEntity);
        userService.createUser(userEntity2);
        mockMvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Shiva Krish"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("shivakrish727@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("Bob Myers"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].email").value("bob@gmail.com"));
    }

    @Test
    public void getUserWorks() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        userService.createUser(userEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", userEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Shiva Krish"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("shivakrish727@gmail.com"));
    }

    @Test
    public void fullUpdateUser() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        userService.createUser(userEntity);
        UserDto updatedDto = UserDto.builder().name("Bob myers").email("bob@gmail.com").build();
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bob myers"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bob@gmail.com"));
    }

    @Test
    public void partialUpdateUser() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        userService.createUser(userEntity);
        UserDto updatedDto = UserDto.builder().email("bob@gmail.com").build();
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", userEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Shiva Krish"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bob@gmail.com"));
    }

    @Test
    public void deleteUser() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        userService.createUser(userEntity);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userEntity.getId()))
                .andExpect(status().isNoContent());
    }

    // ---------- negative paths ----------

    @Test
    public void createUser_blankName_returns400() throws Exception {
        UserDto userDto = UserDto.builder().name("").email("shivakrish727@gmail.com").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_malformedEmail_returns400() throws Exception {
        UserDto userDto = UserDto.builder().name("Shiva Krish").email("not-an-email").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_missingEmail_returns400() throws Exception {
        UserDto userDto = UserDto.builder().name("Shiva Krish").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_duplicateEmail_returns409() throws Exception {
        UserEntity existing = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        userRepository.saveAndFlush(existing);

        UserDto duplicate = UserDto.builder().name("Someone Else").email("shivakrish727@gmail.com").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isConflict());
    }

    @Test
    public void getUser_notFound_returns404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fullUpdateUser_notFound_returns404() throws Exception {
        UserDto userDto = UserDto.builder().name("Bob myers").email("bob@gmail.com").build();
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", 999_999L).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fullUpdateUser_blankName_returns400() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        userService.createUser(userEntity);
        UserDto invalidDto = UserDto.builder().name("").email("bob@gmail.com").build();
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void partialUpdateUser_notFound_returns404() throws Exception {
        UserDto userDto = UserDto.builder().email("bob@gmail.com").build();
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", 999_999L).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser_notFound_returns404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }
}