package com.company.controller;

import com.company.dto.TicketDTO;
import com.company.model.Ticket;
import com.company.service.TicketService;
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
    private final ModelMapper modelMapper;

    @Autowired
    public TicketController(TicketService ticketService, ModelMapper modelMapper) {
        this.ticketService = ticketService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<TicketDTO> purchaseTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        Ticket ticket = convertToEntity(ticketDTO);
        Ticket purchasedTicket = ticketService.purchaseTicket(ticket);
        TicketDTO responseDTO = convertToDto(purchasedTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
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
        return modelMapper.map(ticket, TicketDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
