package com.company.controller;

import com.company.config.TestConfig;
import com.company.dto.UserDTO;
import com.company.model.User;
import com.company.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        when(modelMapper.map(any(UserDTO.class), any())).thenReturn(user);
        when(userService.addUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"firstName\": \"Test\", \"lastName\": \"User\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testGetUserById() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId));
    }

    @Test
    void testRemoveUser() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateUser() throws Exception {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");

        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        // Mock the behavior of getUserById to return the user object
        when(userService.getUserById(userId)).thenReturn(user);

        // Mock the behavior of modelMapper
        when(modelMapper.map(any(UserDTO.class), eq(User.class))).thenReturn(user);

        // Mock the behavior of userService.updateUser
        when(userService.updateUser(userId, user)).thenReturn(user);

        // Perform a GET request to retrieve the user before updating
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) // Print the response to the console
                .andExpect(MockMvcResultMatchers.status().isOk()); // Expect a 200 OK status

        // Perform the PUT request to update the user
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"firstName\": \"Test\", \"lastName\": \"User\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) // Print the response to the console
                .andExpect(MockMvcResultMatchers.status().isOk()); // Expect a 200 OK status
    }

}
