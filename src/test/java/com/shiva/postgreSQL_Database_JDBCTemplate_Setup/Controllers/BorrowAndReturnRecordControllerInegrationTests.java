package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.Controllers;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.AuthorEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BorrowAndReturnRecordEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.AuthorService;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.BookService;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.BorrowAndReturnRecordService;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureMockMvc
public class BorrowAndReturnRecordControllerInegrationTests {
    private ObjectMapper objectMapper;
    private BorrowAndReturnRecordService borrowRecordService;
    private UserService userService;
    private BookService bookService;
    private AuthorService authorService;
    private MockMvc mockMvc;

    @Autowired
    public BorrowAndReturnRecordControllerInegrationTests(ObjectMapper objectMapper, BorrowAndReturnRecordService borrowRecordService,
                                                          UserService userService, BookService bookService, AuthorService authorService, MockMvc mockMvc) {
        this.objectMapper = objectMapper;
        this.borrowRecordService = borrowRecordService;
        this.userService = userService;
        this.bookService = bookService;
        this.authorService = authorService;
        this.mockMvc = mockMvc;
    }

    @Test
    //this is testing if borrowBook works properly by checking httpStatus and Contents
    public void testBorrowBook() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shiva@gmail.com").build();
        userService.createUser(userEntity);
        AuthorEntity author = AuthorEntity.builder().name("JK Rowling").age(52).build();
        authorService.createAuthor(author);
        BooksEntity booksEntity = BooksEntity.builder().isbn("134").title("Harry Potter").author(author).totalCopies(18).availableCopies(10).build();
        bookService.createBook("134", booksEntity);
        mockMvc.perform(MockMvcRequestBuilders.post("/books/{isbn}/borrow/{userId}", booksEntity.getIsbn(), userEntity.getId())
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.book.isbn").value("134"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(userEntity.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.book.availableCopies").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowDate").exists());
    }

    @Test
    //this is testing if returnBook works properly by checking httpStatus and Contents
    public void testReturnBook() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shiva@gmail.com").build();
        userService.createUser(userEntity);
        AuthorEntity author = AuthorEntity.builder().name("JK Rowling").age(52).build();
        authorService.createAuthor(author);
        BooksEntity booksEntity = BooksEntity.builder().isbn("134").title("Harry Potter").author(author).totalCopies(18).availableCopies(10).build();
        bookService.createBook("134", booksEntity);
        BorrowAndReturnRecordEntity borrowAndReturnRecord = borrowRecordService.borrowBook("134", userEntity.getId());
        mockMvc.perform(MockMvcRequestBuilders.put("/borrow-records/{borrowId}/return", borrowAndReturnRecord.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.book.availableCopies").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnDate").value(LocalDate.now().toString()));

    }

    @Test
    //testing if userBorrowHistory works properly by checking HttpStatusCode and contents
    public void testUserBorrowHistory() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shiva@gmail.com").build();
        userService.createUser(userEntity);
        AuthorEntity author = AuthorEntity.builder().name("JK Rowling").age(52).build();
        authorService.createAuthor(author);
        BooksEntity booksEntity = BooksEntity.builder().isbn("134").title("Harry Potter").author(author).totalCopies(18).availableCopies(10).build();
        bookService.createBook("134", booksEntity);
        BorrowAndReturnRecordEntity borrowAndReturnRecord = borrowRecordService.borrowBook("134", userEntity.getId());
        BooksEntity booksEntity2 = BooksEntity.builder().isbn("124").title("Big nate").author(author).totalCopies(18).availableCopies(10).build();
        bookService.createBook("124", booksEntity2);
        BorrowAndReturnRecordEntity borrowAndReturnRecord2 = borrowRecordService.borrowBook("124", userEntity.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/borrow-history", userEntity.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].book.isbn").value("134"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].book.title").value("Harry Potter"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].book.isbn").value("124"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].book.title").value("Big nate"));
    }

    @Test
    //testing if currentBooks works properly by checking HttpStatusCode and contents
    public void testCurrentBooks() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shiva@gmail.com").build();
        userService.createUser(userEntity);
        AuthorEntity author = AuthorEntity.builder().name("JK Rowling").age(52).build();
        authorService.createAuthor(author);
        BooksEntity booksEntity = BooksEntity.builder().isbn("134").title("Harry Potter").author(author).totalCopies(18).availableCopies(10).build();
        bookService.createBook("134", booksEntity);
        BorrowAndReturnRecordEntity borrowAndReturnRecord = borrowRecordService.borrowBook("134", userEntity.getId());
        BooksEntity booksEntity2 = BooksEntity.builder().isbn("124").title("Big nate").author(author).totalCopies(18).availableCopies(10).build();
        bookService.createBook("124", booksEntity2);
        BorrowAndReturnRecordEntity borrowAndReturnRecord2 = borrowRecordService.borrowBook("124", userEntity.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/borrow-records/current"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].book.isbn").value("134"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].book.title").value("Harry Potter"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].book.isbn").value("124"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].book.title").value("Big nate"));

    }

    @Test
    //testing if bookBorrowHistory works properly by checking HttpStatusCode and contents
    public void testBookBorrowHistory() throws Exception {
        UserEntity userEntity = UserEntity.builder().name("Shiva Krish").email("shiva@gmail.com").build();
        UserEntity userEntity2 = UserEntity.builder().name("Bob Teague").email("bob@gmail.com").build();
        userService.createUser(userEntity);
        userService.createUser(userEntity2);
        AuthorEntity author = AuthorEntity.builder().name("JK Rowling").age(52).build();
        authorService.createAuthor(author);
        BooksEntity booksEntity = BooksEntity.builder().isbn("134").title("Harry Potter").author(author).totalCopies(18).availableCopies(10).build();
        bookService.createBook("134", booksEntity);
        BorrowAndReturnRecordEntity borrowAndReturnRecord = borrowRecordService.borrowBook("134", userEntity.getId());
        BorrowAndReturnRecordEntity borrowAndReturnRecord2 = borrowRecordService.borrowBook("134", userEntity2.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/borrow-history", booksEntity.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].book.isbn").value("134"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].book.isbn").value("134"));
    }


}
