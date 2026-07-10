package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    UserEntity createUser(UserEntity userEntity);

    Page<UserEntity> findAllUsers(Pageable pageable);

    Optional<UserEntity> findOneUser(Long id);

    boolean isExists(Long id);

    UserEntity fullUpdateUser(Long id, UserEntity userEntity);

    UserEntity partialUpdateUser(Long id, UserEntity userEntity);

    void delete(Long id);
}
