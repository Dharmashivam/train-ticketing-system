// TicketDTO.java
package com.company.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.IgnoreForBinding;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TicketDTO {

    private Long ticketId;

    @NotNull(message = "User details are required")
    @JsonInclude(Include.NON_NULL)
    private UserDTO user;

    @NotNull(message = "Train details are required")
    private TrainDTO train;

    @NotNull(message = "Price paid must be provided")
    @Min(value = 0, message = "Price paid must be greater than or equal to 0")
    private Double pricePaid;

    private String sectionPreference;

    private int seatNumber;

    public Long getId() {
        return ticketId;
    }

    public void setId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Double getPricePaid() {
        return pricePaid;
    }

    public TrainDTO getTrain() {
        return train;
    }

    public void setTrain(TrainDTO train) {
        this.train = train;
    }

    public void setPricePaid(Double pricePaid) {
        this.pricePaid = pricePaid;
    }

    public String getSectionPreference() {
        return sectionPreference;
    }

    public void setSectionPreference(String sectionPreference) {
        this.sectionPreference = sectionPreference;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;    }

    public int getSeatNumber() {
        return seatNumber;
    }
}
