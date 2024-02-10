package main.java.com.company.service;

import com.company.model.User;

public interface UserService {
    User addUser(User user);
    void removeUser(int userId);
    User updateUser(int userId, User user);
    User getUserById(int userId);
}
