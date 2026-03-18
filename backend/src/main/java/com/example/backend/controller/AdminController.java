package com.example.backend.controller;

import com.example.backend.dto.OrderDto;
import com.example.backend.dto.UserDto;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(OrderService orderService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/orders")
    public List<OrderDto> getAllOrders(@RequestParam(required = false) String status) {
        logger.info("Admin fetching all orders. Status filter: {}", status);
        return orderService.getAllOrders(status)
                .stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        logger.info("Admin creating new user: {}", userDto.getUsername());
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(userDto.getRole());
        User saved = userRepository.save(user);
        return ResponseEntity.status(201).body(new UserDto(saved));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        logger.info("Admin updating user: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(userDto.getRole());
        // Do not update password here, simple example
        User saved = userRepository.save(user);
        return ResponseEntity.ok(new UserDto(saved));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Admin deleting user: {}", id);
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
