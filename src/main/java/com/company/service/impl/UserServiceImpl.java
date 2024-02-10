package com.company.service.impl;

import com.company.model.User;
import com.company.repository.UserRepository;
import com.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean removeUser(Long userId) {
        userRepository.deleteById(userId);
        return false;
    }

    @Override
    public User updateUser(Long userId, User user) {
        user.setId(userId);
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? user.getId() : null;
    }
}
