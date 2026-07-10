package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.repositories;

import com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
//Testing if repository is working
public class UserRepositoryIntegrationTest {
    private UserRepository userRepository;

    @Autowired
    public UserRepositoryIntegrationTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    //test that a user can be created and found in database
    public void test01() {
        UserEntity user = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        //creating author in database
        userRepository.save(user);
        Optional<UserEntity> result = userRepository.findById(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    //testing if you can find all users in database
    public void test02() {
        UserEntity user = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        UserEntity user2 = UserEntity.builder().name("Bob Stein").email("bob@gmail.com").build();
        userRepository.save(user);
        userRepository.save(user2);
        Iterable<UserEntity> result = userRepository.findAll();
        assertThat(result).hasSize(2);
        assertThat(result).contains(user, user2);
    }

    @Test
    //testing if you can update a user in the database
    public void test03() {
        UserEntity user = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        userRepository.save(user);
        user.setEmail("shivakrish@gmail.com");
        userRepository.save(user);
        Optional<UserEntity> result = userRepository.findById(user.getId());
        assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    //testing if you can delete a user from the database
    public void test04() {
        UserEntity user = UserEntity.builder().name("Shiva Krish").email("shivakrish727@gmail.com").build();
        userRepository.save(user);
        userRepository.delete(user);
        Optional<UserEntity> result = userRepository.findById(user.getId());
        assertThat(result).isEmpty();
    }


}
