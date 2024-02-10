package main.java.com.company.controller;

import main.java.com.company.dto.TicketDTO;
import main.java.com.company.model.Ticket;
import main.java.com.company.service.TicketService;
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
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    @Autowired
    public TicketController(TicketService ticketService, ModelMapper modelMapper) {
        this.ticketService = ticketService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<TicketDTO> purchaseTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        Ticket ticket = convertToEntity(ticketDTO);
        Optional<Ticket> purchasedTicket = ticketService.purchaseTicket(ticket);
        return purchasedTicket.map(t -> ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(t)))
                              .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDTO> getTicketDetails(@PathVariable @Positive(message = "Ticket ID must be a positive integer") Long ticketId) {
        Optional<Ticket> ticket = ticketService.getTicketDetails(ticketId);
        return ticket.map(t -> ResponseEntity.ok(convertToDto(t)))
                    .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketDTO> modifyTicket(@PathVariable @Positive(message = "Ticket ID must be a positive integer") Long ticketId, @Valid @RequestBody TicketDTO ticketDTO) {
        Ticket ticket = convertToEntity(ticketDTO);
        Optional<Ticket> modifiedTicket = ticketService.modifyTicket(ticketId, ticket);
        return modifiedTicket.map(t -> ResponseEntity.ok(convertToDto(t)))
                              .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> cancelTicket(@PathVariable @Positive(message = "Ticket ID must be a positive integer") Long ticketId) {
        Optional<Ticket> ticket = ticketService.getTicketDetails(ticketId);
        if (ticket.isPresent()) {
            ticketService.cancelTicket(ticketId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Method to convert TicketDTO to Ticket entity
    private Ticket convertToEntity(TicketDTO ticketDTO) {
        return modelMapper.map(ticketDTO, Ticket.class);
    }

    // Method to convert Ticket entity to TicketDTO
    private TicketDTO convertToDto(Ticket ticket) {
        return modelMapper.map(ticket, TicketDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}