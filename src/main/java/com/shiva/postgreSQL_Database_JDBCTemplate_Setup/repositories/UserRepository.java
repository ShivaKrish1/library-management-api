package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long>,
        JpaRepository<UserEntity, Long> {
}
