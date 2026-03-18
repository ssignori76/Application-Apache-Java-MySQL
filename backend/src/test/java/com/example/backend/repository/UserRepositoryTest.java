package com.example.backend.repository;

import com.example.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("pass");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole("CUSTOMER");
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("testuser");
        assertTrue(found.isPresent());
    }
}
