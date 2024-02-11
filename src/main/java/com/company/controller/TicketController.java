package com.company.controller;

import com.company.dto.*;
import com.company.model.Ticket;
import com.company.model.Train;
import com.company.model.User;
import com.company.model.Section;
import com.company.service.TicketService;
import com.company.service.TrainService;
import com.company.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        try {
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
            TicketDTO responseDTO = convertTicketToDto(purchasedTicket);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (ConstraintViolationException ex) {
            // Handle validation errors
            StringBuilder errorMessage = new StringBuilder();
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        return optionalTicket.map(ticket -> ResponseEntity.ok(convertTicketToDto(ticket)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("user-tickets/{userId}")
    public ResponseEntity<UserAndTicketsDTO> getUserTickets(@PathVariable @Positive(message = "User ID must be a positive integer") Long userId) {
        try {
            // Retrieve all tickets associated with the user ID
            List<Ticket> tickets = ticketService.getUserTickets(userId);

            // If no tickets found, return not found status
            if (tickets.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Get user details from any ticket (assuming the user details are the same for all tickets)
            UserDTO userDTO = convertUserToDto(tickets.get(0).getUser());

            // Convert tickets to DTOs without including the user field
            List<TicketDTO> ticketDTOs = tickets.stream()
                    .map(this::convertTicketToDtoWithoutUser)
                    .collect(Collectors.toList());

            // Create a DTO containing user details and ticket details
            UserAndTicketsDTO responseDTO = new UserAndTicketsDTO();
            responseDTO.setUser(userDTO);
            responseDTO.setTickets(ticketDTOs);

            // Return the DTO
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            // Handle any unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/users-and-seats/{trainId}/{section}")
    public ResponseEntity<List<UserAndSeatDTO>> getUsersAndSeatsBySection(
            @PathVariable @Positive(message = "Train ID must be a positive integer") Long trainId,
            @PathVariable String section) {
        try {
            // Convert the section string to an enum if possible
            Section sectionEnum = Section.valueOf(section.toUpperCase());

            // Retrieve all tickets for the given train ID and section
            List<Ticket> tickets = ticketService.getTicketsByTrainIdAndSection(trainId, sectionEnum);

            // If no tickets found, return an empty list
            if (tickets.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Convert tickets to DTOs containing user details and allocated seats
            List<UserAndSeatDTO> userAndSeatDTOs = tickets.stream()
                    .map(this::convertTicketToUserAndSeatDTO)
                    .collect(Collectors.toList());

            // Return the DTO list
            return ResponseEntity.ok(userAndSeatDTOs);
        } catch (IllegalArgumentException e) {
            // Handle invalid section parameter
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Handle any unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{ticketId}")
    public ResponseEntity<?> modifyTicket(@PathVariable @Positive(message = "Ticket ID must be a positive integer") Long ticketId,
                                                  @RequestParam @Positive(message = "User ID must be a positive integer") Long userId) {

        try {
            Optional<Ticket> optionalTicket = Optional.ofNullable(ticketService.getTicketDetails(ticketId));
            if (optionalTicket.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid TicketId"); // Ticket not found
            }

            Ticket ticket = optionalTicket.get();
            Train train = ticket.getTrain();
            User user = ticket.getUser();

            // Check if the user ID provided in the request matches the user associated with the ticket
            if (!user.getId().equals(userId)) {
                return ResponseEntity.badRequest().body("UserId Invalid");
            }

            // Check if the requested section change is valid
            Section newSection = (ticket.getSectionPreference() == Section.A) ? Section.B : Section.A;

            // Check if the requested section has available seats
            int newSectionSeatCapacity = (newSection == Section.A) ? train.getSectionASeatCapacity() : train.getSectionBSeatCapacity();
            if (newSectionSeatCapacity <= 0) {
                return ResponseEntity.badRequest().body("No seats available to change");
            }

            // Update seat capacities and last assigned seat numbers based on the section change
            if (ticket.getSectionPreference() == Section.A) {
                train.setSectionASeatCapacity(train.getSectionASeatCapacity() + 1);
                train.setSectionBSeatCapacity(train.getSectionBSeatCapacity() - 1);
                train.setLastAssignedSeatNumberSectionA(train.getLastAssignedSeatNumberSectionA() - 1);
                train.setLastAssignedSeatNumberSectionB(train.getLastAssignedSeatNumberSectionB() + 1);
            } else {
                train.setSectionASeatCapacity(train.getSectionASeatCapacity() - 1);
                train.setSectionBSeatCapacity(train.getSectionBSeatCapacity() + 1);
                train.setLastAssignedSeatNumberSectionA(train.getLastAssignedSeatNumberSectionA() + 1);
                train.setLastAssignedSeatNumberSectionB(train.getLastAssignedSeatNumberSectionB() - 1);
            }

            // Update the ticket with the new section preference
            ticket.setSectionPreference(newSection);

            // Update the train with the modified seat capacities and last assigned seat numbers
            trainService.updateSeatCapacities(train.getTrainId(), train);

            // Update the ticket in the database
            Ticket modifiedTicket = ticketService.modifyTicket(ticketId, ticket);
            TicketDTO responseDTO = convertTicketToDto(modifiedTicket);
            return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Invalid section preference. Must be 'A' or 'B'");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Error occurred while modifying the ticket
    }
    }


    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> cancelTicket(@PathVariable @Positive(message = "Ticket ID must be a positive integer") Long ticketId) {
        try {
            ticketService.cancelTicket(ticketId);
            return ResponseEntity.noContent().build(); // Ticket cancelled successfully
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Error occurred while cancelling the ticket
        }
    }

    // Method to convert TicketDTO to Ticket entity
    private Ticket convertToEntity(TicketDTO ticketDTO) {
        return modelMapper.map(ticketDTO, Ticket.class);
    }

    // Method to convert Ticket entity to TicketDTO
    private TicketDTO convertTicketToDto(Ticket ticket) {
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);
        return ticketDTO;
    }

    private UserDTO convertUserToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private TrainDTO convertTrainToDto(Train train) {
        return modelMapper.map(train, TrainDTO.class);
    }

    private TicketDTO convertTicketToDtoWithoutUser(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setSeatNumber(ticket.getSeatNumber());
        ticketDTO.setPricePaid(ticket.getPricePaid());
        ticketDTO.setSectionPreference(ticket.getSectionPreference().toString());
        ticketDTO.setTrain(convertTrainToDto(ticket.getTrain()));
        ticketDTO.setUser(null);
        return ticketDTO;
    }

    private UserAndSeatDTO convertTicketToUserAndSeatDTO(Ticket ticket) {
        UserDTO userDTO = convertUserToDto(ticket.getUser());
        UserAndSeatDTO userAndSeatDTO = new UserAndSeatDTO();
        userAndSeatDTO.setUser(userDTO);
        userAndSeatDTO.setSeatNumber(ticket.getSeatNumber());
        return userAndSeatDTO;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<String> handleValidationExceptions(Exception ex) {
        StringBuilder errorMessage = new StringBuilder();
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manve = (MethodArgumentNotValidException) ex;
            manve.getBindingResult().getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("; "));
        } else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) ex;
            cve.getConstraintViolations().forEach(violation -> errorMessage.append(violation.getMessage()).append("; "));
        }
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }
}
