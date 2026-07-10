package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BorrowAndReturnRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowandReturnRecordRepository extends JpaRepository<BorrowAndReturnRecordEntity, Long> {
    //Following are custom queries the Spring JPA Automatically developed

    //retrieve list of borrow records that belong to a particular user by user id
    List<BorrowAndReturnRecordEntity> findByUserId(Long id);

    //retrieve list of borrow records that belong to a particular book by book isbn
    List<BorrowAndReturnRecordEntity> findByBookIsbn(String isbn);

    //returns all borrow records where returnDate is NULL in the database.
    List<BorrowAndReturnRecordEntity> findByReturnDateIsNull();
}
