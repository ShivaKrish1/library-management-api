package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.AuthorEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class BookRepositoryIntegrationTests {
    private BooksRepository undertest;

    @Autowired
    public BookRepositoryIntegrationTests(BooksRepository undertest) {
        this.undertest = undertest;
    }

    @Test
    //test the book can be created and recalled in database
    public void test01() {
        AuthorEntity author = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        /*you don't need to create author in database since we have cascade set up (whatever ahppens to book
        also due to author)*/
        BooksEntity book = BooksEntity.builder()
                .isbn("201")
                .title("Wimpy Kid")
                .author(author)
                .totalCopies(5)
                .availableCopies(3)
                .build();
        //bc of cascade this also creates an outhor
        undertest.save(book);
        //undertest.find(book.getIsbn());
        Optional<BooksEntity> result = undertest.findById(book.getIsbn());
        book.getAuthor().setId(result.get().getAuthor().getId());
        //checks to see if book is present in database
        assertThat(result).isPresent();
        //is result = to book
        assertThat(result.get()).isEqualTo(book);
    }


    @Test
    //testing that multiple books can be created and recalled in database
    public void test02() {
        AuthorEntity author = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        BooksEntity book1 = BooksEntity.builder()
                .isbn("200")
                .title("Wimpy Kid")
                .author(author)
                .totalCopies(7)
                .availableCopies(3)
                .build();
        BooksEntity book2 = BooksEntity.builder()
                .isbn("201")
                .title("Big Nate")
                .author(author)
                .totalCopies(20)
                .availableCopies(14)
                .build();
        BooksEntity book3 = BooksEntity.builder()
                .isbn("202")
                .title("Wimpy Kid")
                .author(author)
                .totalCopies(3)
                .availableCopies(0)
                .build();
        book1 = undertest.save(book1);
        AuthorEntity savedAuthor = book1.getAuthor();
        book2.setAuthor(savedAuthor);
        book3.setAuthor(savedAuthor);
        undertest.save(book2);
        undertest.save(book3);
        Iterable<BooksEntity> result = undertest.findAll();
        assertThat(result).hasSize(3);
        assertThat(result).contains(book1, book2, book3);
    }

    @Test
    //testing that a book can be updated in the database
    public void test03() {
        AuthorEntity author = AuthorEntity.builder()
                .name("Jack")
                .age(24)
                .build();
        BooksEntity book = BooksEntity.builder()
                .isbn("201")
                .title("Wimpy Kid")
                .author(author)
                .totalCopies(7)
                .availableCopies(3)
                .build();
        book = undertest.save(book);
        book.setTitle("Forcsha");
        book = undertest.save(book);
        Optional<BooksEntity> result = undertest.findByIsbn("201");
        assertThat(result.get()).isEqualTo(book);
    }

    //
    @Test
    //testing that a book can be deleted
    public void test04() {
        AuthorEntity author = AuthorEntity.builder()
                .name("Jack")
                .age(24)
                .build();
        BooksEntity book = BooksEntity.builder()
                .isbn("201")
                .title("Wimpy Kid")
                .author(author)
                .totalCopies(7)
                .availableCopies(3)
                .build();
        undertest.save(book);
        //this is main test
        undertest.deleteById(book.getIsbn());
        Optional<BooksEntity> result = undertest.findByIsbn(book.getIsbn());
        //should be empty cause that means its been deleted
        assertThat(result).isEmpty();
    }


}
