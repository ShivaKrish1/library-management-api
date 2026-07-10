package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.impl;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories.UserRepository;
import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }

    @Override
    public Page<UserEntity> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<UserEntity> findOneUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserEntity fullUpdateUser(Long id, UserEntity userEntity) {
        userEntity.setId(id);
        return userRepository.save(userEntity);
    }

    @Override
    //id is accosiated with current user, userEntity is new info we are going to input
    public UserEntity partialUpdateUser(Long id, UserEntity userEntity) {
        Optional<UserEntity> savedUserEntity = userRepository.findById(id);
        return savedUserEntity.map(savedUserEntity1 -> {
            if (userEntity.getEmail() != null) {
                savedUserEntity1.setEmail(userEntity.getEmail());
            }
            if (userEntity.getName() != null) {
                savedUserEntity1.setName(userEntity.getName());
            }
            return userRepository.save(savedUserEntity1);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
