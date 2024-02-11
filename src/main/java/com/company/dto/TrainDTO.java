package com.company.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class TrainDTO {

    @NotEmpty(message = "Train ID is required")
    @Min(value = 1, message = "Train ID must be a positive number")
    private Long trainId;

    @NotEmpty(message = "From location must be provided")
    private String fromLocation;

    @NotEmpty(message = "To location must be provided")
    private String toLocation;

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }
}
