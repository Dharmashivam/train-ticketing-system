package com.company.controller;

import com.company.dto.TicketDTO;
import com.company.dto.UserDTO;
import com.company.model.Ticket;
import com.company.model.Train;
import com.company.model.User;
import com.company.service.TicketService;
import com.company.service.TrainService;
import com.company.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;
@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TrainService trainService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public TicketController(TicketService ticketService, TrainService trainService, UserService userService, ModelMapper modelMapper) {
        this.ticketService = ticketService;
        this.trainService = trainService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<?> purchaseTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        // Check if the email already exists in the system
        Long userId = userService.getUserIdByEmail(ticketDTO.getUser().getEmail());
        if (userId == null) {
            // If the user doesn't exist, create a new user
            UserDTO userDTO = ticketDTO.getUser();
            User user = new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
            userId = userService.addUser(user).getId();
        }

        // Retrieve the train details using the provided train ID
        Optional<Train> optionalTrain = Optional.ofNullable(trainService.getTrainById(ticketDTO.getTrain().getTrainId()));
        if (optionalTrain.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid train ID: " + ticketDTO.getTrain().getTrainId());
        }
        Train train = optionalTrain.get();
        // Check if the provided 'from' and 'to' match the train's route
        if (!ticketDTO.getTrain().getFromLocation().equals(train.getFromLocation()) || !ticketDTO.getTrain().getToLocation().equals(train.getToLocation())) {
            return ResponseEntity.badRequest().body("Invalid 'from' or 'to' station for the given train");
        }

        // Check if the section preference is provided
        if (ticketDTO.getSectionPreference() != null && !ticketDTO.getSectionPreference().isEmpty()) {
            // Check seat availability for the preferred section
            if (ticketDTO.getSectionPreference().equalsIgnoreCase("A")) {
                if (train.getSectionASeatCapacity() <= 0) {
                    return ResponseEntity.badRequest().body("No seats available in section Aa");
                }
            } else if (ticketDTO.getSectionPreference().equalsIgnoreCase("B")) {
                if (train.getSectionBSeatCapacity() <= 0) {
                    return ResponseEntity.badRequest().body("No seats available in section Bb");
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid section preference. Must be 'A' or 'B'");
            }
        } else {
            // If no section preference is provided, assign a random section with available seats
            if (train.getSectionASeatCapacity() > 0) {

                ticketDTO.setSectionPreference("A");
            } else if (train.getSectionBSeatCapacity() > 0) {
                ticketDTO.setSectionPreference("B");
            } else {
                return ResponseEntity.badRequest().body("No seats available in any section");
            }
        }

        int seatNumber = generateSeatNumber(train, ticketDTO.getSectionPreference());
        if (seatNumber == -1) {
            return ResponseEntity.badRequest().body("No seats available in the selected section");
        }

        // Decrease the seat capacity in the chosen section
        if (ticketDTO.getSectionPreference().equalsIgnoreCase("A")) {
            train.setSectionASeatCapacity(train.getSectionASeatCapacity() - 1);
        } else {
            train.setSectionBSeatCapacity(train.getSectionBSeatCapacity() - 1);
        }
        trainService.updateSeatCapacities(train.getTrainId(), train);
        // Create the ticket and save it
        Ticket ticket = convertToEntity(ticketDTO);
        ticket.setUser(userService.getUserById(userId));
        ticket.setSeatNumber(seatNumber);
        Ticket purchasedTicket = ticketService.purchaseTicket(ticket);
        TicketDTO responseDTO = convertToDto(purchasedTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    private int generateSeatNumber(Train train, String sectionPreference) {
        int lastAssignedSeatNumber;
        int sectionCapacity;
        if (sectionPreference.equalsIgnoreCase("A")) {
            lastAssignedSeatNumber = train.getLastAssignedSeatNumberSectionA();
            sectionCapacity = train.getSectionASeatCapacity();
            if (sectionCapacity -1 < 0) {
                return -1; // No seats available in section A
            }
            train.setLastAssignedSeatNumberSectionA(lastAssignedSeatNumber + 1);
            return lastAssignedSeatNumber + 1;
        } else if (sectionPreference.equalsIgnoreCase("B")) {
            lastAssignedSeatNumber = train.getLastAssignedSeatNumberSectionB();
            sectionCapacity = train.getSectionBSeatCapacity();
            if (sectionCapacity -1 < 0) {
                return -1; // No seats available in section B
            }
            train.setLastAssignedSeatNumberSectionB(lastAssignedSeatNumber + 1);
            return lastAssignedSeatNumber + 1;
        } else {
            return -1; // Invalid section preference
        }
    }



    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDTO> getTicketDetails(@PathVariable @Positive(message = "Ticket ID must be a positive integer") Long ticketId) {
        Optional<Ticket> optionalTicket = Optional.ofNullable(ticketService.getTicketDetails(ticketId));
        return optionalTicket.map(ticket -> ResponseEntity.ok(convertToDto(ticket)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketDTO> modifyTicket(@PathVariable @Positive(message = "Ticket ID must be a positive integer") Long ticketId, @Valid @RequestBody TicketDTO ticketDTO) {
        Ticket ticket = convertToEntity(ticketDTO);
        Ticket modifiedTicket = ticketService.modifyTicket(ticketId, ticket);
        TicketDTO responseDTO = convertToDto(modifiedTicket);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> cancelTicket(@PathVariable @Positive(message = "Ticket ID must be a positive integer") Long ticketId) {
        ticketService.cancelTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    // Method to convert TicketDTO to Ticket entity
    private Ticket convertToEntity(TicketDTO ticketDTO) {
        return modelMapper.map(ticketDTO, Ticket.class);
    }

    // Method to convert Ticket entity to TicketDTO
    private TicketDTO convertToDto(Ticket ticket) {
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);
        return ticketDTO;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
