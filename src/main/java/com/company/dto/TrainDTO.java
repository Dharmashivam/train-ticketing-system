package com.company.dto;

import javax.validation.constraints.NotNull;

public class TrainDTO {

    private Long trainId;

    @NotNull(message = "From location must be provided")
    private String fromLocation;

    @NotNull(message = "To location must be provided")
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
