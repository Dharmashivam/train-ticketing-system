package com.company.controller;

import com.company.config.TestConfig;
import com.company.dto.TicketDTO;
import com.company.dto.TrainDTO;
import com.company.dto.UserAndTicketsDTO;
import com.company.dto.UserDTO;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    void testPurchaseTicket() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");

        TrainDTO trainDTO = new TrainDTO();
        trainDTO.setFromLocation("From");
        trainDTO.setToLocation("To");

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setUser(userDTO);
        ticketDTO.setTrain(trainDTO);
        ticketDTO.setSectionPreference("A");
        ticketDTO.setPricePaid(50.00);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        Train train = new Train();
        train.setTrainId(1L);
        train.setFromLocation("From");
        train.setToLocation("To");
        train.setLastAssignedSeatNumberSectionA(0);
        train.setLastAssignedSeatNumberSectionB(0);
        train.setSectionASeatCapacity(1);
        train.setSectionBSeatCapacity(1);

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTrain(train);
        ticket.setSectionPreference(Section.A);

        when(modelMapper.map(any(TicketDTO.class), any())).thenReturn(ticket);
        when(ticketService.purchaseTicket(any(Ticket.class))).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user\": {\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john@example.com\"}, \"train\": {\"trainId\": \"1L\", \"fromLocation\": \"From\", \"toLocation\": \"To\"}, \"pricePaid\": \"50.00\", \"sectionPreference\": \"A\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testGetTicketDetails() throws Exception {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        when(ticketService.getTicketDetails(ticketId)).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/{ticketId}", ticketId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ticketId));
    }

    @Test
    void testGetUserTickets() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());

        when(ticketService.getUserTickets(userId)).thenReturn(tickets);
        when(modelMapper.map(any(User.class), any())).thenReturn(new UserDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/user-tickets/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetUsersAndSeatsBySection() throws Exception {
        Long trainId = 1L;
        String section = "A";

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());

        when(ticketService.getTicketsByTrainIdAndSection(trainId, Section.valueOf(section.toUpperCase()))).thenReturn(tickets);
        when(modelMapper.map(any(User.class), any())).thenReturn(new UserDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/users-and-seats/{trainId}/{section}", trainId, section))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testModifyTicket() throws Exception {
        Long ticketId = 1L;
        Long userId = 1L;

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");

        TrainDTO trainDTO = new TrainDTO();

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setUser(userDTO);
        ticketDTO.setTrain(trainDTO);
        ticketDTO.setSectionPreference("A");

        Ticket ticket = new Ticket();
        ticket.setUser(new User("John", "Doe", "john@example.com"));
        ticket.setTrain(new Train());
        ticket.setSectionPreference(Section.A);

        when(modelMapper.map(any(TicketDTO.class), any())).thenReturn(ticket);
        when(ticketService.modifyTicket(ticketId, ticket)).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.put("/tickets/{ticketId}?userId={userId}", ticketId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user\": {\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john@example.com\"}, \"train\": {}, \"sectionPreference\": \"A\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCancelTicket() throws Exception {
        Long ticketId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/tickets/{ticketId}", ticketId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
