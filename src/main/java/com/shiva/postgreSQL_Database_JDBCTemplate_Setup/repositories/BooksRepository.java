package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.BooksEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksRepository extends CrudRepository<BooksEntity, String>,
        PagingAndSortingRepository<BooksEntity, String> {
    Optional<BooksEntity> findByIsbn(String isbn);
}
