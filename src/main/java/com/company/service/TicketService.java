package com.company.service;

import com.company.model.Section;
import com.company.model.Ticket;

import java.util.List;

public interface TicketService {
    Ticket purchaseTicket(Ticket ticket);
    void cancelTicket(Long ticketId);
    Ticket modifyTicket(Long ticketId, Ticket ticket);
    Ticket getTicketDetails(Long ticketId);

    List<Ticket> getUserTickets(Long userId);

    List<Ticket> getTicketsByTrainIdAndSection(Long trainId, Section section);
}
