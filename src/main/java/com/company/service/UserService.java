package com.company.service;

import com.company.model.User;

public interface UserService {
    User addUser(User user);
    boolean removeUser(Long userId);
    User updateUser(Long userId, User user);
    User getUserById(Long userId);
    Long getUserIdByEmail(String email);
}