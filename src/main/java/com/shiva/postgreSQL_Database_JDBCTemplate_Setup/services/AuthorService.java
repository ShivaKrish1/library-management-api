package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthorService {

    AuthorEntity createAuthor(AuthorEntity authorEntity);

    Page<AuthorEntity> findAll(Pageable pageable);

    Optional<AuthorEntity> returnAuthorById(Long id);

    AuthorEntity updateAuthor(Long id, AuthorEntity authorEntity);

    boolean isExists(Long id);

    AuthorEntity partialUpdateAuthor(Long id, AuthorEntity authorEntity);

    void deleteAuthor(Long id);
}
