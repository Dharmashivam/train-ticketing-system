package main.java.com.company.service;

import com.company.model.Ticket;

public interface TicketService {
    Ticket purchaseTicket(Ticket ticket);
    void cancelTicket(Long ticketId);
    Ticket modifyTicket(Long ticketId, Ticket ticket);
    Ticket getTicketDetails(Long ticketId);
}
