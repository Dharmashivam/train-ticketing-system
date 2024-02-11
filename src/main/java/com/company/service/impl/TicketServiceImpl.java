package com.company.service.impl;

import com.company.model.Section;
import com.company.model.Ticket;
import com.company.model.Train;
import com.company.repository.TicketRepository;
import com.company.service.TicketService;
import com.company.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TrainService trainService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TrainService trainService) {
        this.ticketRepository = ticketRepository;
        this.trainService = trainService;
    }

    @Override
    public Ticket purchaseTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void cancelTicket(Long ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        optionalTicket.ifPresent(ticket -> {
            ticketRepository.deleteById(ticketId);
            updateTrainSeatCapacity(ticket);
        });
    }

    private void updateTrainSeatCapacity(Ticket ticket) {
        Train train = ticket.getTrain();
        if (train != null) {
            Section section = ticket.getSectionPreference();
            if (section == Section.A) {
                train.setSectionASeatCapacity(train.getSectionASeatCapacity() + 1);
                train.setLastAssignedSeatNumberSectionA(train.getLastAssignedSeatNumberSectionA() - 1);
            } else if (section == Section.B) {
                train.setSectionBSeatCapacity(train.getSectionBSeatCapacity() + 1);
                train.setLastAssignedSeatNumberSectionB(train.getLastAssignedSeatNumberSectionB() - 1);
            }
            trainService.updateSeatCapacities(train.getTrainId(), train);
        }
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
