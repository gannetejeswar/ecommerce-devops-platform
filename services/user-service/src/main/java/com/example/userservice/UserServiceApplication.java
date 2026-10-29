package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@RestController
@RequestMapping("/api/users")
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong(0);

    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        if (request.name() == null || request.name().isBlank()
                || request.email() == null || request.email().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Name and email are required"
            );
        }

        long id = ids.incrementAndGet();
        User user = new User(id, request.name(), request.email());
        users.put(id, user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return users.values().stream()
            .sorted((a, b) -> Long.compare(a.id(), b.id()))
            .toList();
    }

    public record CreateUserRequest(String name, String email) {}
    public record User(Long id, String name, String email) {}
}
