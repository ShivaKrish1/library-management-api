package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.controllers;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.BorrowAndReturnRecordDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BorrowAndReturnRecordEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.mapper;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.BookService;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.BorrowAndReturnRecordService;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BorrowAndReturnRecordController {

    private BorrowAndReturnRecordService borrowRecordService;
    private BookService bookService;
    private UserService userService;
    private mapper<BorrowAndReturnRecordEntity, BorrowAndReturnRecordDto> mapper;

    public BorrowAndReturnRecordController(BorrowAndReturnRecordService borrowRecordService, BookService bookService, UserService userService, mapper<BorrowAndReturnRecordEntity, BorrowAndReturnRecordDto> mapper) {
        this.borrowRecordService = borrowRecordService;
        this.bookService = bookService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping(path = "/books/{isbn}/borrow/{userId}")
    public ResponseEntity<BorrowAndReturnRecordDto> borrowBook(@PathVariable("isbn") String isbn, @PathVariable("userId") Long userId) {
        if (!bookService.isExists(isbn) || !userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BorrowAndReturnRecordEntity borrowRecord = borrowRecordService.borrowBook(isbn, userId);
        if (borrowRecord.getBook().getAvailableCopies() <= 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        BorrowAndReturnRecordDto result = mapper.mapto(borrowRecord);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/borrow-records/{borrowId}/return")
    public ResponseEntity<BorrowAndReturnRecordDto> returnBook(@PathVariable("borrowId") Long borrowId) {
        BorrowAndReturnRecordEntity borrowAndReturnRecordEntity = borrowRecordService.returnBook(borrowId);
        if (borrowAndReturnRecordEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BorrowAndReturnRecordDto result = mapper.mapto(borrowAndReturnRecordEntity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/borrow-history")
    public ResponseEntity<List<BorrowAndReturnRecordDto>> userBorrowHistory(@PathVariable("id") Long id) {
        List<BorrowAndReturnRecordEntity> entityResult = borrowRecordService.getBorrowHistory(id);
        if (entityResult == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<BorrowAndReturnRecordDto> result = new ArrayList<>();
        for (BorrowAndReturnRecordEntity entry : entityResult) {
            result.add(mapper.mapto(entry));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    //all books in library currently being borrowed
    @GetMapping("/borrow-records/current")
    public ResponseEntity<List<BorrowAndReturnRecordDto>> currentBooks() {
        List<BorrowAndReturnRecordEntity> entityList = borrowRecordService.getCurrentBorrowedBooks();
        List<BorrowAndReturnRecordDto> result = new ArrayList<>();
        for (BorrowAndReturnRecordEntity entry : entityList) {
            result.add(mapper.mapto(entry));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //borrow history of one specific book
    @GetMapping("/books/{isbn}/borrow-history")
    public ResponseEntity<List<BorrowAndReturnRecordDto>> bookBorrowHistory(@PathVariable("isbn") String isbn) {
        List<BorrowAndReturnRecordEntity> entityResult = borrowRecordService.getBookBorrowHistory(isbn);
        if (entityResult == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<BorrowAndReturnRecordDto> result = new ArrayList<>();
        for (BorrowAndReturnRecordEntity entry : entityResult) {
            result.add(mapper.mapto(entry));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
