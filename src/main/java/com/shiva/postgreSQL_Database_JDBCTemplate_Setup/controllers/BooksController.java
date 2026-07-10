package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.controllers;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto.BooksDto;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers.mapper;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class BooksController {
    private BookService bookService;
    private mapper<BooksEntity, BooksDto> mapper;

    public BooksController(BookService bookService, mapper<BooksEntity, BooksDto> mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @PutMapping("/books/{isbn}")
    //creating or Updating book based on if it already exists
    public ResponseEntity<BooksDto> createOrUpdateBook(@PathVariable("isbn") String isbn, @RequestBody
    BooksDto booksDto) {
        //create
        if (!bookService.isExists(isbn)) {
            BooksEntity bookEntity = mapper.mapfrom(booksDto);
            BooksEntity savedBookEntity = bookService.createBook(isbn, bookEntity);
            return new ResponseEntity<>(mapper.mapto(savedBookEntity), HttpStatus.CREATED);
        }
        //update
        BooksEntity booksEntity = mapper.mapfrom(booksDto);
        BooksEntity savedBookEntity = bookService.updateBook(isbn, booksEntity);
        BooksDto result = mapper.mapto(savedBookEntity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/books")
    public Page<BooksDto> listBooks(Pageable pageable) {
        //make a Page of bookEntities
        Page<BooksEntity> booksEntityList = bookService.findall(pageable);
        // Convert each BookEntity into a BookDto while preserving pagination information
        return booksEntityList.map(mapper::mapto);
    }


    //get a bookDto by Isbn
    @GetMapping("/books/{isbn}")
    public ResponseEntity<BooksDto> getBookbyIsbn(@PathVariable("isbn") String isbn) {
        Optional<BooksEntity> foundBook = bookService.getBookByIsbn(isbn);
        //converts to bookDto from Optional<BookEntity> and gets book
        return foundBook.map(booksEntity -> {
            //book found
            BooksDto booksDto = mapper.mapto(booksEntity);
            return new ResponseEntity<>(booksDto, HttpStatus.OK);
            //book doesn't exist
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //partial update of book
    @PatchMapping("/books/{isbn}")
    public ResponseEntity<BooksDto> partialBookUpdate(@PathVariable("isbn") String isbn, @RequestBody BooksDto booksDto) {
        if (!bookService.isExists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BooksEntity booksEntity = mapper.mapfrom(booksDto);
        BooksEntity savedBook = bookService.partiallyUpdate(isbn, booksEntity);
        BooksDto result = mapper.mapto(savedBook);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //deleting a book
    @DeleteMapping("/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn) {
        if (!bookService.isExists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/books/{isbn}/totalCopies")
    public ResponseEntity<Integer> totalCopies(@PathVariable("isbn") String isbn) {
        if (!bookService.isExists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Integer totalCopies = bookService.getTotalCopies(isbn);
        return new ResponseEntity<>(totalCopies, HttpStatus.OK);
    }

    @GetMapping("/books/{isbn}/availableCopies")
    public ResponseEntity<Integer> availableCopies(@PathVariable("isbn") String isbn) {
        if (!bookService.isExists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Integer availableCopies = bookService.getAvailableCopies(isbn);
        return new ResponseEntity<>(availableCopies, HttpStatus.OK);
    }

    @GetMapping("/books/{isbn}/borrowedCopies")
    public ResponseEntity<Integer> borrowedCopies(@PathVariable("isbn") String isbn) {
        if (!bookService.isExists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Integer borrowedCopies = bookService.getBorrowedCopies(isbn);
        return new ResponseEntity<>(borrowedCopies, HttpStatus.OK);
    }


}
