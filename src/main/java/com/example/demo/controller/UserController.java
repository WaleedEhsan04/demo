package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@Tag(name = "User-Controller")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    @Operation(
            summary = "Returns a list of all users",
            tags = "User-Controller"
    )
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("user/{id}")
    @Operation(
            summary = "Returns a single user for a given id",
            tags = "User-Controller"
    )
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping("user")
    @Operation(
            summary = "Creates a user",
            tags = "User-Controller"
    )
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("user/{id}")
    @Operation(
            summary = "Updates an existing user",
            tags = "User-Controller"
    )
    public ResponseEntity<User> update(@RequestBody User updatedUser, @PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        user.get().setFirstName(updatedUser.getFirstName());
        user.get().setLastName(updatedUser.getLastName());
        user.get().setEmail(updatedUser.getEmail());

        User updated = userService.saveUser(user.get());
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }

    @DeleteMapping("user/{id}")
    @Operation(
            summary = "Deletes a user",
            tags = "User-Controller"
    )
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
