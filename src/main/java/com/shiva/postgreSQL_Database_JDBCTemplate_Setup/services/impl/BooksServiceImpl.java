package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.impl;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories.BooksRepository;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksServiceImpl implements BookService {
    BooksRepository booksRepository;

    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public BooksEntity createBook(String isbn, BooksEntity booksEntity) {
        booksEntity.setIsbn(isbn);
        return booksRepository.save(booksEntity);
    }

    @Override
    public List<BooksEntity> getAllBooks() {
        List<BooksEntity> result = (List<BooksEntity>) booksRepository.findAll();
        return result;
    }

    @Override
    public Optional<BooksEntity> getBookByIsbn(String isbn) {
        return booksRepository.findByIsbn(isbn);
    }

    @Override
    public boolean isExists(String isbn) {
        return booksRepository.existsById(isbn);
    }

    @Override
    public BooksEntity updateBook(String isbn, BooksEntity booksEntity) {
        booksEntity.setIsbn(isbn);
        return booksRepository.save(booksEntity);
    }

    @Override
    public BooksEntity partiallyUpdate(String isbn, BooksEntity booksEntity) {
        //getting book with isbn
        Optional<BooksEntity> book = booksRepository.findByIsbn(isbn);
        //setting the book with specific isbn to the booksEntity field the user provides
        return book.map(result -> {
            if (booksEntity.getTitle() != null) {
                result.setTitle(booksEntity.getTitle());
            }
            if (booksEntity.getTotalCopies() != null) {
                result.setTotalCopies(booksEntity.getTotalCopies());
            }
            if (booksEntity.getAvailableCopies() != null) {
                result.setAvailableCopies(booksEntity.getAvailableCopies());
            }
            //saving it
            return booksRepository.save(result);
        }).orElse(null);
    }

    @Override
    public void delete(String isbn) {
        booksRepository.deleteById(isbn);
    }

    @Override
    //Returns a page of books based on the requested page number and page size.
    public Page<BooksEntity> findall(Pageable pageable) {
        return booksRepository.findAll(pageable);
    }


    @Override
    public Integer getTotalCopies(String isbn) {
        if (!booksRepository.existsById(isbn)) {
            return null;
        }
        BooksEntity booksEntity = booksRepository.findByIsbn(isbn).orElse(null);
        return booksEntity.getTotalCopies();

    }

    //Following are some statistics about books
    @Override
    public Integer getAvailableCopies(String isbn) {
        if (!booksRepository.existsById(isbn)) {
            return null;
        }
        BooksEntity booksEntity = booksRepository.findByIsbn(isbn).orElse(null);
        return booksEntity.getAvailableCopies();

    }

    @Override
    public Integer getBorrowedCopies(String isbn) {
        if (!booksRepository.existsById(isbn)) {
            return null;
        }
        BooksEntity booksEntity = booksRepository.findByIsbn(isbn).orElse(null);
        Integer borrowedCopies = booksEntity.getTotalCopies() - booksEntity.getAvailableCopies();
        return borrowedCopies;
    }


}
