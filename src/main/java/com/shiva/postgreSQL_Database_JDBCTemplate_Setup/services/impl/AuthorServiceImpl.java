package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.impl;


import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.AuthorEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories.AuthorRepository;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

//this is where you make all the methods using the AuthorRepository
@Service
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity createAuthor(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    @Override
    public Page<AuthorEntity> findAll(Pageable pageable) {
        return authorRepository.findAll((org.springframework.data.domain.Pageable) pageable);
    }

    @Override
    public Optional<AuthorEntity> returnAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public AuthorEntity updateAuthor(Long id, AuthorEntity authorEntity) {
        authorEntity.setId(id);
        return authorRepository.save(authorEntity);
    }

    @Override
    public boolean isExists(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdateAuthor(Long id, AuthorEntity authorEntity) {
        Optional<AuthorEntity> savedAuthorEntity = authorRepository.findById(id);
        return savedAuthorEntity.map(existingAuthor -> {
            if (authorEntity.getAge() != null) {
                existingAuthor.setAge(authorEntity.getAge());
            }
            if (authorEntity.getName() != null) {
                existingAuthor.setName(authorEntity.getName());
            }
            return authorRepository.save(existingAuthor);
        }).orElse(null);

    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }


}
