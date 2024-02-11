package com.company.service.impl;

import com.company.model.Section;
import com.company.model.Ticket;
import com.company.repository.TicketRepository;
import com.company.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket purchaseTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void cancelTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public Ticket modifyTicket(Long ticketId, Ticket ticket) {
        ticket.setId(ticketId);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketDetails(Long ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }

    @Override
    public List<Ticket> getUserTickets(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Override
    public List<Ticket> getTicketsByTrainIdAndSection(Long trainId, Section section) {
        return ticketRepository.findByTrainIdAndSection(trainId, section);
    }
}
