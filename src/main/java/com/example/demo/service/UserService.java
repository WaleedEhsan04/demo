package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        try{
            return userRepository.save(user);
        }catch(DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("A user with email: " + user.getEmail() + " already exists");
        }
    }

    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new NotFoundException("No such value exists with id: " + id);
        }
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        try{
            userRepository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new NotFoundException("No such value exists with id: " + id);
        }

    }
}