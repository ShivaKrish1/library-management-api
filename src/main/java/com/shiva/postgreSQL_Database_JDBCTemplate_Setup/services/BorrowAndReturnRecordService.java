package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BorrowAndReturnRecordEntity;

import java.util.List;


public interface BorrowAndReturnRecordService {

    BorrowAndReturnRecordEntity borrowBook(String isbn, Long userId);

    BorrowAndReturnRecordEntity returnBook(Long borrowId);

    //retrieving borrow history of user by their id
    List<BorrowAndReturnRecordEntity> getBorrowHistory(Long userId);

    //retriving current borrowed books(return date = null)
    List<BorrowAndReturnRecordEntity> getCurrentBorrowedBooks();

    //retrieve all borrow records for book by its isbn
    List<BorrowAndReturnRecordEntity> getBookBorrowHistory(String isbn);


}
