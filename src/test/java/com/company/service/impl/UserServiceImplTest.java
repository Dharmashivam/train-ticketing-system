package com.company.service.impl;

import com.company.model.User;
import com.company.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.addUser(user);
        assertEquals(user.getId(), savedUser.getId());
    }

    @Test
    void testRemoveUser() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);
        userService.removeUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateUser(userId, user);
        assertEquals(userId, updatedUser.getId());
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        User retrievedUser = userService.getUserById(userId);
        assertEquals(userId, retrievedUser.getId());
    }

    @Test
    void testGetUserIdByEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Long userId = userService.getUserIdByEmail(email);
        assertEquals(1L, userId);
    }
}
