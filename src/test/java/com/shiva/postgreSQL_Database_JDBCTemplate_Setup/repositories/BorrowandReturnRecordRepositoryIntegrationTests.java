package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.AuthorEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BorrowAndReturnRecordEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class BorrowandReturnRecordRepositoryIntegrationTests {

    private BorrowandReturnRecordRepository borrowRecordRepository;
    private UserRepository userRepository;
    private BooksRepository booksRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public BorrowandReturnRecordRepositoryIntegrationTests(BorrowandReturnRecordRepository borrowRecordRepository, UserRepository userRepository
            , BooksRepository booksRepository, AuthorRepository authorRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.userRepository = userRepository;
        this.booksRepository = booksRepository;
        this.authorRepository = authorRepository;
    }

    @Test
// tests if a borrow record can be saved correctly
    public void testSaveBorrowRecord() {
        UserEntity user = UserEntity.builder()
                .name("Shiva krish")
                .email("shivakrish727@gmail.com")
                .build();
        userRepository.save(user);

        AuthorEntity author = AuthorEntity.builder()
                .name("Joshua Bloch")
                .age(60)
                .build();

        BooksEntity book = BooksEntity.builder()
                .isbn("1234567890")
                .title("Effective Java")
                .author(author)
                .totalCopies(5)
                .availableCopies(5)
                .build();
        booksRepository.save(book);

        BorrowAndReturnRecordEntity borrowRecord = BorrowAndReturnRecordEntity.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .returnDate(null)
                .build();

        BorrowAndReturnRecordEntity result = borrowRecordRepository.save(borrowRecord);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
        assertThat(result.getBook().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(result.getBorrowDate()).isEqualTo(LocalDate.now());
        assertThat(result.getReturnDate()).isNull();
    }

    //testing findByUserID
    @Test
    public void findByUserId() {
        UserEntity user = UserEntity.builder()
                .name("Shiva krish")
                .email("shivakrish727@gmail.com")
                .build();
        user = userRepository.save(user);

        AuthorEntity author = AuthorEntity.builder()
                .name("Joshua Bloch")
                .age(60)
                .build();
        author = authorRepository.save(author);

        BooksEntity book = BooksEntity.builder()
                .isbn("1234567890")
                .title("Effective Java")
                .author(author)
                .totalCopies(5)
                .availableCopies(5)
                .build();
        book = booksRepository.save(book);

        BooksEntity book2 = BooksEntity.builder()
                .isbn("123456789")
                .title("Effective")
                .author(author)
                .totalCopies(5)
                .availableCopies(5)
                .build();
        book2 = booksRepository.save(book2);

        BorrowAndReturnRecordEntity borrowRecord = BorrowAndReturnRecordEntity.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .build();
        borrowRecord = borrowRecordRepository.save(borrowRecord);

        BorrowAndReturnRecordEntity borrowRecord2 = BorrowAndReturnRecordEntity.builder()
                .user(user)
                .book(book2)
                .borrowDate(LocalDate.now())
                .build();
        borrowRecord2 = borrowRecordRepository.save(borrowRecord2);

        List<BorrowAndReturnRecordEntity> result =
                borrowRecordRepository.findByUserId(user.getId());

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(borrowRecord, borrowRecord2);
    }

    //testing deleteBorrowRecord
    @Test
    public void deleteBorrowRecord() {
        UserEntity user = UserEntity.builder()
                .name("Shiva krish")
                .email("shivakrish727@gmail.com")
                .build();
        userRepository.save(user);

        AuthorEntity author = AuthorEntity.builder()
                .name("Joshua Bloch")
                .age(60)
                .build();

        BooksEntity book = BooksEntity.builder()
                .isbn("1234567890")
                .title("Effective Java")
                .author(author)
                .totalCopies(5)
                .availableCopies(5)
                .build();
        booksRepository.save(book);

        BorrowAndReturnRecordEntity borrowRecord = BorrowAndReturnRecordEntity.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .returnDate(null)
                .build();
        BorrowAndReturnRecordEntity result = borrowRecordRepository.save(borrowRecord);

        borrowRecordRepository.delete(result);

        Optional<BorrowAndReturnRecordEntity> deleted =
                borrowRecordRepository.findById(result.getId());

        assertThat(deleted).isEmpty();

    }


}
