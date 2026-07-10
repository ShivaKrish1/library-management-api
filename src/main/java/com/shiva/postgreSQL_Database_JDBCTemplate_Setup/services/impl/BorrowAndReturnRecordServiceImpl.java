package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.impl;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BorrowAndReturnRecordEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories.AuthorRepository;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories.BooksRepository;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories.BorrowandReturnRecordRepository;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories.UserRepository;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.BorrowAndReturnRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowAndReturnRecordServiceImpl implements BorrowAndReturnRecordService {

    private BorrowandReturnRecordRepository borrowRecordRepository;
    private UserRepository userRepository;
    private BooksRepository booksRepository;
    private AuthorRepository authorRepository;

    public BorrowAndReturnRecordServiceImpl(BorrowandReturnRecordRepository borrowRecordRepository, UserRepository userRepository
            , BooksRepository booksRepository, AuthorRepository authorRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.userRepository = userRepository;
        this.booksRepository = booksRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    //isbn reps the book, and userId reps user that will be borrowing book
    public BorrowAndReturnRecordEntity borrowBook(String isbn, Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        BooksEntity book = booksRepository.findByIsbn(isbn).orElse(null);
        if (book == null || user == null || (book.getAvailableCopies() <= 0)) {
            return null;
        }
        BorrowAndReturnRecordEntity borrowRecord = BorrowAndReturnRecordEntity.builder().borrowDate(LocalDate.now()).user(user).book(book).build();
        //now that its been borrowed there is one less copy available
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        //saving book since we updated
        booksRepository.save(book);
        //saving borrowed record in repository then returning it
        return borrowRecordRepository.save(borrowRecord);
    }

    @Override
    public BorrowAndReturnRecordEntity returnBook(Long borrowId) {
        BorrowAndReturnRecordEntity borrowedRecord =
                borrowRecordRepository.findById(borrowId).orElse(null);
        if (borrowedRecord == null || borrowedRecord.getReturnDate() != null) {
            return null;
        }
        //we set it equal to now because when the method is called the purpose is the user is returning it at the moment
        borrowedRecord.setReturnDate(LocalDate.now());
        borrowedRecord.getBook().setAvailableCopies(borrowedRecord.getBook().getAvailableCopies() + 1);
        booksRepository.save(borrowedRecord.getBook());
        return borrowRecordRepository.save(borrowedRecord);
    }

    //retrieving borrow history of user by their id
    @Override
    public List<BorrowAndReturnRecordEntity> getBorrowHistory(Long userId) {
        if (!userRepository.existsById(userId)) {
            return null;
        }
        List<BorrowAndReturnRecordEntity> result = borrowRecordRepository.findByUserId(userId);
        return result;
    }

    //retriving current borrowed books(return date = null)
    @Override
    public List<BorrowAndReturnRecordEntity> getCurrentBorrowedBooks() {
        List<BorrowAndReturnRecordEntity> result = borrowRecordRepository.findByReturnDateIsNull();
        return result;
    }

    //retrieve all borrow records for book by its isbn
    @Override
    public List<BorrowAndReturnRecordEntity> getBookBorrowHistory(String isbn) {
        if (!booksRepository.existsById(isbn)) {
            return null;
        }
        List<BorrowAndReturnRecordEntity> result = borrowRecordRepository.findByBookIsbn(isbn);
        return result;
    }


}
