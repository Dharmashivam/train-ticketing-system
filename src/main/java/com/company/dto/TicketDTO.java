package com.company.dto;

import com.company.model.Section;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class TicketDTO {

    private Long id;

    @NotEmpty(message = "User information is required")
    private UserDTO user;

    @NotEmpty(message = "Price paid must be provided")
    @Min(value = 0, message = "Price paid must be greater than or equal to 0")
    private double pricePaid;

    @NotEmpty(message = "Section must be provided")
    private Section section;

    @NotEmpty(message = "Seat number must be provided")
    @Min(value = 1, message = "Seat number must be greater than or equal to 1")
    private int seatNumber;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
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
}