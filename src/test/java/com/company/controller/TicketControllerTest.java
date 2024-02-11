package com.company.controller;

import com.company.config.TestConfig;
import com.company.dto.TicketDTO;
import com.company.dto.TrainDTO;
import com.company.dto.UserAndTicketsDTO;
import com.company.dto.UserDTO;
import com.company.dto.UserAndSeatDTO;
import com.company.model.Ticket;
import com.company.model.Train;
import com.company.model.User;
import com.company.model.Section;
import com.company.service.TicketService;
import com.company.service.TrainService;
import com.company.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketController ticketController;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private TrainService trainService;

    @MockBean
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void purchaseTicket_HappyPath() {
        // Arrange
        String email = "test@test.com";
        String firstName = "John";
        String lastName = "Doe";
        String fromLocation = "New York";
        String toLocation = "Chicago";
        Long trainId = 1L;
        String sectionPreference = "A";
        int seatNumber = 1;
        double pricePaid = 100.0;
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        TrainDTO trainDTO = new TrainDTO();
        trainDTO.setTrainId(trainId);
        trainDTO.setFromLocation(fromLocation);
        trainDTO.setToLocation(toLocation);
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setUser(userDTO);
        ticketDTO.setTrain(trainDTO);
        ticketDTO.setSectionPreference(sectionPreference);
        ticketDTO.setSeatNumber(seatNumber);
        ticketDTO.setPricePaid(pricePaid);

        // Mock UserService
        User mockUser = new User(email, firstName, lastName);
        mockUser.setId(1L); // Set a mock user ID
        when(userService.addUser(any(User.class))).thenReturn(mockUser); // Return the mock user
        when(userService.getUserIdByEmail(email)).thenReturn(null); // Return null to simulate a new user

        // Mock TrainService
        Train mockTrain = new Train(fromLocation, toLocation, 0, 0);
        mockTrain.setSectionASeatCapacity(1);
        mockTrain.setSectionBSeatCapacity(1);
        mockTrain.setTrainId(trainId);
        when(trainService.getTrainById(trainId)).thenReturn(mockTrain);

        // Act
        ticketController.purchaseTicket(ticketDTO);

        // Assert
        verify(userService, times(1)).getUserIdByEmail(email);
        verify(userService, times(1)).addUser(any(User.class));
        verify(trainService, times(1)).getTrainById(trainId);
        verify(trainService, times(1)).updateSeatCapacities(eq(trainId), any(Train.class)); // Ensure updateSeatCapacities is invoked
        verify(ticketService, times(1)).purchaseTicket(any(Ticket.class));
    }


    @Test
    void getTicketDetails_HappyPath() {
        // Arrange
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        when(ticketService.getTicketDetails(ticketId)).thenReturn(ticket);

        // Act
        ResponseEntity<TicketDTO> response = ticketController.getTicketDetails(ticketId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ticketService, times(1)).getTicketDetails(ticketId);
    }

    @Test
    void getUserTickets_HappyPath() {
        // Arrange
        Long userId = 1L;
        List<Ticket> tickets = new ArrayList<>();
        when(ticketService.getUserTickets(userId)).thenReturn(tickets);

        // Act
        ResponseEntity<UserAndTicketsDTO> response = ticketController.getUserTickets(userId);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), response);
        verify(ticketService, times(1)).getUserTickets(userId);
    }

    @Test
    void getUsersAndSeatsBySection_HappyPath() {
        // Arrange
        Long trainId = 1L;
        String section = "A";
        List<UserAndSeatDTO> userAndSeatDTOs = new ArrayList<>();
        when(ticketService.getTicketsByTrainIdAndSection(trainId, Section.A)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<UserAndSeatDTO>> response = ticketController.getUsersAndSeatsBySection(trainId, section);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), response);
        verify(ticketService, times(1)).getTicketsByTrainIdAndSection(trainId, Section.A);
    }

    @Test
    void modifyTicket_HappyPath() {
        // Arrange
        Long ticketId = 1L;
        Long userId = 1L;
        Long trainId = 1L;
        String email = "test@test.com";
        String firstName = "John";
        String lastName = "Doe";
        String fromLocation = "New York";
        String toLocation = "Chicago";
        double pricePaid = 100.0;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setSectionPreference(Section.A);
        ticket.setSeatNumber(1);
        ticket.setPricePaid(pricePaid);
        Train train = new Train(fromLocation, toLocation, 1, 1);
        train.setLastAssignedSeatNumberSectionA(1);
        train.setLastAssignedSeatNumberSectionB(1);
        train.setTrainId(trainId);
        User user = new User(email, firstName, lastName);
        user.setId(userId);
        ticket.setUser(user);
        ticket.setTrain(train);
        when(ticketService.getTicketDetails(ticketId)).thenReturn(ticket);
        when(ticketService.modifyTicket(ticketId, ticket)).thenReturn(ticket);

        // Act
        ResponseEntity<?> response = ticketController.modifyTicket(ticketId, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody()); // Ensure response body is not null
        assertTrue(response.getBody() instanceof TicketDTO); // Ensure response body is of type TicketDTO
        // Add more assertions to verify the content of the response body if needed
        verify(ticketService, times(1)).getTicketDetails(ticketId);
        verify(ticketService, times(1)).modifyTicket(ticketId, ticket);
    }


    @Test
    void cancelTicket_HappyPath() {
        // Arrange
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        Train train = new Train();
        ticket.setTrain(train);
        when(ticketService.getTicketDetails(ticketId)).thenReturn(ticket);

        // Act
        ResponseEntity<Void> response = ticketController.cancelTicket(ticketId);

        // Assert
        assertEquals(ResponseEntity.noContent().build(), response);
        verify(ticketService, times(1)).getTicketDetails(ticketId);
        verify(ticketService, times(1)).cancelTicket(ticketId);
    }
}
