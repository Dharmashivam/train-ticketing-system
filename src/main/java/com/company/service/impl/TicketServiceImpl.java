package main.java.com.company.service.impl;

import com.company.model.Ticket;
import main.java.com.company.repository.TicketRepository;
import main.java.com.company.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Optional<Ticket> modifyTicket(Long ticketId, Ticket ticket) {
        ticket.setId(ticketId);
        return Optional.ofNullable(ticketRepository.save(ticket));
    }

    @Override
    public Optional<Ticket> getTicketDetails(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }
}