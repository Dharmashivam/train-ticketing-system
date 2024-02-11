package com.company.service;

import com.company.model.Section;
import com.company.model.Ticket;
import com.company.repository.TicketRepository;
import com.company.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPurchaseTicket() {
        Ticket ticket = new Ticket();
        when(ticketRepository.save(any())).thenReturn(ticket);

        Ticket savedTicket = ticketService.purchaseTicket(new Ticket());

        assertNotNull(savedTicket);
        assertEquals(ticket, savedTicket);
        verify(ticketRepository, times(1)).save(any());
    }

    @Test
    void testCancelTicket() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        ticketService.cancelTicket(ticketId);

        verify(ticketRepository, times(1)).deleteById(ticketId);
    }

    @Test
    void testModifyTicket() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        when(ticketRepository.save(any())).thenReturn(ticket);

        Ticket modifiedTicket = ticketService.modifyTicket(ticketId, new Ticket());

        assertNotNull(modifiedTicket);
        assertEquals(ticket, modifiedTicket);
        verify(ticketRepository, times(1)).save(any());
    }

    @Test
    void testGetTicketDetails() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        Ticket retrievedTicket = ticketService.getTicketDetails(ticketId);

        assertNotNull(retrievedTicket);
        assertEquals(ticket, retrievedTicket);
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void testGetUserTickets() {
        Long userId = 1L;
        List<Ticket> tickets = new ArrayList<>();
        when(ticketRepository.findByUserId(userId)).thenReturn(tickets);

        List<Ticket> retrievedTickets = ticketService.getUserTickets(userId);

        assertNotNull(retrievedTickets);
        assertEquals(tickets, retrievedTickets);
        verify(ticketRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetTicketsByTrainIdAndSection() {
        Long trainId = 1L;
        Section section = Section.A;
        List<Ticket> tickets = new ArrayList<>();
        when(ticketRepository.findByTrainIdAndSection(trainId, section)).thenReturn(tickets);

        List<Ticket> retrievedTickets = ticketService.getTicketsByTrainIdAndSection(trainId, section);

        assertNotNull(retrievedTickets);
        assertEquals(tickets, retrievedTickets);
        verify(ticketRepository, times(1)).findByTrainIdAndSection(trainId, section);
    }
}
