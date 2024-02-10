package com.company.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Price paid must be provided")
    @Min(value = 0, message = "Price paid must be greater than or equal to 0")
    private Double pricePaid;

    @Column(nullable = false)
    private int seatNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Section  sectionPreference;

    public Ticket() {
    }

    public Ticket(Train train, User user, Double pricePaid, int seatNumber, Section  sectionPreference) {
        this.train = train;
        this.user = user;
        this.pricePaid = pricePaid;
        this.seatNumber = seatNumber;
        this.sectionPreference = sectionPreference;
    }


    public Long getId() {
        return ticketId;
    }

    public void setId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Section  getSectionPreference() {
        return sectionPreference;
    }

    public void setSectionPreference(Section sectionPreference) {
        this.sectionPreference = sectionPreference;
    }

    public void setPricePaid(Double pricePaid) {
        this.pricePaid = pricePaid;
    }
}
