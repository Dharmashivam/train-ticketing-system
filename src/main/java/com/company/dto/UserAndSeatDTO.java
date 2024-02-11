package com.company.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class UserAndSeatDTO {

    @NotEmpty(message = "User details are required")
    private UserDTO user;

    @Positive(message = "Seat number must be a positive integer")
    private int seatNumber;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
