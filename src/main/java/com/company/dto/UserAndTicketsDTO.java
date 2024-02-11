package com.company.dto;

import java.util.List;

public class UserAndTicketsDTO {
    private UserDTO user;
    private List<TicketDTO> tickets;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }
}
