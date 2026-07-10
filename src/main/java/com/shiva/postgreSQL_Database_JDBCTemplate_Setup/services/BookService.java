package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BooksEntity createBook(String isbn, BooksEntity booksEntity);

    List<BooksEntity> getAllBooks();

    Optional<BooksEntity> getBookByIsbn(String isbn);

    boolean isExists(String isbn);

    BooksEntity updateBook(String isbn, BooksEntity booksEntity);

    BooksEntity partiallyUpdate(String isbn, BooksEntity booksEntity);

    void delete(String isbn);

    Page<BooksEntity> findall(Pageable pageable);

    Integer getTotalCopies(String isbn);

    Integer getAvailableCopies(String isbn);

    Integer getBorrowedCopies(String isbn);


}
