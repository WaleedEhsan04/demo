package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "waleed", "ehsan", "waleed.ehsan@gmail.com");
    }

    @Test
    void should_get_all_users() {
        // given
        User secondUser = new User(2L,"John","Smith","john.smith@gmail.com");
        given(userRepository.findAll()).willReturn(List.of(user, secondUser));

        //when
        List<User> userList = userService.getAllUsers();

        //then
        assertEquals(userList.size(), 2);
    }

    @Test
    void should_get_user_with_given_id() {
        // given
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // when
        Optional<User> retrievedUser = userService.getUserById(user.getId());

        // then
        assertTrue(retrievedUser.isPresent());
        assertEquals(retrievedUser.get(), user);
    }
    @Test
    void should_throw_exception_when_getting_a_user_that_does_not_exist() {

        // given
        long id = 999L;
        given(userRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> userService.getUserById(id), "No such value exists with id: " + id);
    }

    @Test
    void should_save_a_user() {
        // given
        given(userRepository.save(user)).willReturn(user);

        // when
        User savedUser = userService.saveUser(user);

        // then
        assertEquals(savedUser, user);
    }

    @Test
    void should_throw_exception_when_saving_user_with_duplicate_email() {
        // given
        given(userRepository.save(user)).willThrow(new DataIntegrityViolationException("A user with email: " + user.getEmail() + " already exists"));

        // then
        assertThrows(DataIntegrityViolationException.class, () -> userService.saveUser(user), "A user with email: " + user.getEmail() + " already exists");
    }

    @Test
    void should_delete_a_user() {
        // given
        long id = user.getId();

        // when
        userService.deleteUser(id);

        // then
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void should_throw_exception_when_deleting_a_user_that_does_not_exist() {
        // given
        long id = user.getId();

        //when
        doThrow(new EmptyResultDataAccessException(1)).when(userRepository).deleteById(id);

        // then
        assertThrows(NotFoundException.class, () -> userService.deleteUser(id), "No such value exists with id: " + id);
    }
}