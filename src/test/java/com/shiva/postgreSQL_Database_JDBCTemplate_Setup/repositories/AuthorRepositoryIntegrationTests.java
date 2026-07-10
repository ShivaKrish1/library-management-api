package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.AuthorEntity;
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
//THESE TESTS ARE FOR THE PERSISTENCE LAYER
public class AuthorRepositoryIntegrationTests {

    private AuthorRepository undertest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository authorRepository) {
        this.undertest = authorRepository;
    }

    @Test
    //test that an author can be created and found in the database
    public void test01() {
        //first step is we need an author
        AuthorEntity author = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        //this should create author in database
        undertest.save(author);
        //this should find the author
        Optional<AuthorEntity> result = undertest.findById(author.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);


    }

    @Test
    //test that multiple authors can be created and recalled in the database
    public void test02() {
        AuthorEntity author1 = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        AuthorEntity author2 = AuthorEntity.builder()
                .name("Bob")
                .age(21)
                .build();
        AuthorEntity author3 = AuthorEntity.builder()
                .name("Rose")
                .age(19)
                .build();
        undertest.save(author1);
        undertest.save(author2);
        undertest.save(author3);
        Iterable<AuthorEntity> results = undertest.findAll();
        assertThat(results).hasSize(3);
        assertThat(results).contains(author1, author2, author3);

    }

    @Test
    //test that author can be updated
    public void test03() {
        AuthorEntity author = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();

        undertest.save(author);

        author.setName("UPDATA");
        undertest.save(author);

        Optional<AuthorEntity> result = undertest.findById(author.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(author.getId());
    }

    @Test
    //test that an author can be deleted
    public void test04() {
        AuthorEntity author = AuthorEntity.builder()
                .name("Shiva")
                .age(20)
                .build();
        undertest.save(author);
        undertest.deleteById(author.getId());
        Optional<AuthorEntity> result = undertest.findById(author.getId());
        assertThat(result).isEmpty();
    }


}
