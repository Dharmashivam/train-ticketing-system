package com.company.model;

import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "ID must be greater than 0")
    private Long id;

    @NotNull(message = "User information is required")
    @Valid
    @ManyToOne
    @JoinColumn(name = "user_id") // Assuming there's a user_id column in the ticket table
    private User user;

    @NotNull(message = "Price paid must be provided")
    @Min(value = 0, message = "Price paid must be greater than or equal to 0")
    private double pricePaid;

    @NotNull(message = "Section must be provided")
    private Section section;

    @NotNull(message = "Seat number must be provided")
    @Min(value = 1, message = "Seat number must be greater than or equal to 1")
    private int seatNumber;

    public Ticket() {
    }

    public Ticket(User user, double pricePaid, Section section, int seatNumber) {
        this.user = user;
        this.pricePaid = pricePaid;
        this.section = section;
        this.seatNumber = seatNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", user=" + user +
                ", pricePaid=" + pricePaid +
                ", section=" + section +
                ", seatNumber=" + seatNumber +
                '}';
    }
}