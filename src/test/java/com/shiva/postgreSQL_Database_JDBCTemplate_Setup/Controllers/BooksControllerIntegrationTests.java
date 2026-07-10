package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.Controllers;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.BooksDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.BookService;
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
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
public class BooksControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BookService bookService;

    @Autowired
    public BooksControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.bookService = bookService;
    }

    @Test
    //should return true if http 201 if successfully created
    public void testCreateBookReturnsCorrectHttp() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Fearless").author(null).totalCopies(7).availableCopies(3).build();
        String bookjson = objectMapper.writeValueAsString(booksEntity);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/{isbn}", "1234")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookjson)).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    //should return true if contents of book match
    public void testCreateBookReturnsCreatedBook() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Fearless").author(null).totalCopies(7).availableCopies(3).build();
        String bookjson = objectMapper.writeValueAsString(booksEntity);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/{isbn}", "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookjson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Fearless"))
                .andExpect((MockMvcResultMatchers.jsonPath("$.isbn").value("1234")
                ));
    }

    @Test
    //should return true if http 200 is success (get)
    public void testGetAllBooksReturnsCorrectHttp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    //should return the contents of book if success
    public void testGetAllBooksContents() throws Exception {
        BooksEntity booksEntity1 = BooksEntity.builder().isbn("1234").title("Fearless").author(null).totalCopies(7).availableCopies(3).build();
        BooksEntity booksEntity2 = BooksEntity.builder().isbn("234").title("Wimpy Kid").author(null).totalCopies(7).availableCopies(3).build();
        bookService.createBook(booksEntity1.getIsbn(), booksEntity1);
        bookService.createBook(booksEntity2.getIsbn(), booksEntity2);
        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value("Fearless"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].title").value("Wimpy Kid"));
    }

    @Test
    //testing if correct HTTP is returned
    public void returnBookByIsbnSuccessfulHTTP() throws Exception {
        BooksEntity booksEntity1 = BooksEntity.builder().isbn("1234").title("Fearless").author(null).totalCopies(7).availableCopies(3).build();
        bookService.createBook(booksEntity1.getIsbn(), booksEntity1);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", booksEntity1.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    //testing if correct content is returned
    public void returnBookByIsbnSuccessfulContent() throws Exception {
        BooksEntity booksEntity1 = BooksEntity.builder().isbn("1234").title("Fearless").author(null).totalCopies(7).availableCopies(3).build();
        bookService.createBook(booksEntity1.getIsbn(), booksEntity1);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", booksEntity1.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Fearless"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("1234"));
    }

    @Test
    //testing if correct http is returned
    public void fullUpdateBookSuccessfulHTTP() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Wimpy Kid").author(null).totalCopies(7).availableCopies(3).build();
        BooksEntity savedBook = bookService.createBook("1234", booksEntity);
        BooksDto updatedBook = BooksDto.builder().isbn("1234").title("Big Nate").author(null).build();
        mockMvc.perform(MockMvcRequestBuilders.put("/books/{isbn}", savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk());
    }

    @Test
    //testing if correct content is returned
    public void fullUpdateBookSuccessfulContent() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Wimpy Kid").author(null).totalCopies(7).availableCopies(3).build();
        BooksEntity savedBook = bookService.createBook("1234", booksEntity);
        BooksDto updatedBook = BooksDto.builder().isbn("1234").title("Big Nate").author(null).build();
        mockMvc.perform(MockMvcRequestBuilders.put("/books/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Big Nate"));
    }

    @Test
    //testing if correct http is returned
    public void partialUpdateSuccessfulHttp() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Wimpy Kid").author(null).totalCopies(7).availableCopies(3).build();
        BooksEntity savedBook = bookService.createBook("1234", booksEntity);
        BooksDto updatedBook = BooksDto.builder().title("Big Nate").build();
        mockMvc.perform(MockMvcRequestBuilders.patch("/books/{isbn}", savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk());
    }

    @Test
    //testing if correct content is returned
    public void partialUpdateSuccesfulContent() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Wimpy Kid").author(null).totalCopies(7).availableCopies(3).build();
        BooksEntity savedBook = bookService.createBook("1234", booksEntity);
        BooksDto updatedBook = BooksDto.builder().title("Big Nate").build();
        mockMvc.perform(MockMvcRequestBuilders.patch("/books/{isbn}", savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Big Nate"));
    }

    @Test
    //testing if correct http is returned
    public void deleteBookSuccessfulHttp() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Wimpy Kid").author(null).totalCopies(7).availableCopies(3).build();
        BooksEntity savedBook = bookService.createBook("1234", booksEntity);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{isbn}", savedBook.getIsbn()))
                .andExpect(status().isNoContent());
    }

    @Test
    //testing if totalCopies returns correct HttpStatusCode and contents
    public void testTotalCopies() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Wimpy Kid").totalCopies(7).availableCopies(3).build();
        bookService.createBook("1234", booksEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/totalCopies", booksEntity.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("7"));
    }

    @Test
    //testing if availableCopies returns correct HttpStatusCode and contents
    public void testAvailableCopies() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Wimpy Kid").totalCopies(7).availableCopies(3).build();
        bookService.createBook("1234", booksEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/availableCopies", booksEntity.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("3"));
    }

    @Test
    //testing if borrowedCopies returns correct HttpStatusCode and contents
    public void testBorrowCopies() throws Exception {
        BooksEntity booksEntity = BooksEntity.builder().isbn("1234").title("Wimpy Kid").totalCopies(7).availableCopies(3).build();
        bookService.createBook("1234", booksEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/borrowedCopies", booksEntity.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("4"));
    }

}


