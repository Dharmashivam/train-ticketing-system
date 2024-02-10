package com.company.controller;

import com.company.dto.UserDTO;
import com.company.model.User;
import com.company.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            User user = convertToEntity(userDTO);
            User addedUser = userService.addUser(user);
            UserDTO responseDTO = convertToDto(addedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email " + userDTO.getEmail() + " already exists.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable(required = true) long userId) {
        if (userId <= 0) {
            return ResponseEntity.badRequest().body("User ID must be a positive integer.");
        }

        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(userId));
        return optionalUser.map(user -> ResponseEntity.ok(convertToDto(user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable(required = true) Long userId) {
        if (userId <= 0) {
            return ResponseEntity.badRequest().body("User ID must be a positive integer.");
        }

        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(userId));
        return optionalUser.map(user -> {
            userService.removeUser(userId);
            return ResponseEntity.ok("User with ID " + userId + " has been successfully removed.");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + userId + " not found."));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        if (userId <= 0) {
            return ResponseEntity.badRequest().body("User ID must be a positive integer.");
        }

        Optional<User> existingUserOptional = Optional.ofNullable(userService.getUserById(userId));
        return existingUserOptional.map(existingUser -> {
            User user = convertToEntity(userDTO);
            if (existingUser.equals(user)) {
                return ResponseEntity.ok("No changes were made.");
            }
            try {
                User updatedUser = userService.updateUser(userId, user);
                UserDTO responseDTO = convertToDto(updatedUser);
                return ResponseEntity.ok(responseDTO);
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.badRequest().body("User with provided email already exists.");
            }
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + userId + " not found."));
    }

    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }


}
