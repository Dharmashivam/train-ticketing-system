package main.java.com.company.controller;

import main.java.com.company.dto.UserDTO;
import main.java.com.company.model.User;
import main.java.com.company.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
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
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        Optional<User> addedUser = userService.addUser(user);
        return addedUser.map(u -> ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(u)))
                        .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable @Positive(message = "User ID must be a positive integer") Long userId) {
        Optional<Boolean> removedUser = userService.removeUser(userId);
        return removedUser.map(removed -> removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build())
                          .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable @Positive(message = "User ID must be positive integer") Long userId, @Valid @RequestBody UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        Optional<User> updatedUser = userService.updateUser(userId, user);
        return updatedUser.map(u -> ResponseEntity.ok(convertToDto(u)))
                          .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable @Positive(message = "User ID must be a positive integer") Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(u -> ResponseEntity.ok(convertToDto(u)))
                   .orElse(ResponseEntity.notFound().build());
    }

    // Method to convert UserDTO to User entity
    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    // Method to convert User entity to UserDTO
    private UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
