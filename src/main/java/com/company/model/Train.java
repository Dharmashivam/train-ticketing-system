package com.company.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

@Entity
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromLocation;

    private String toLocation;

    @Min(value = 0)
    private int sectionASeatCapacity;

    @Min(value = 0)
    private int sectionBSeatCapacity;

    private int lastAssignedSeatNumberSectionA;

    private int lastAssignedSeatNumberSectionB;

    public Train() {
        // Default constructor
    }

    public Train(String fromLocation, String toLocation, int sectionASeatCapacity, int sectionBSeatCapacity) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.sectionASeatCapacity = sectionASeatCapacity;
        this.sectionBSeatCapacity = sectionBSeatCapacity;
        this.lastAssignedSeatNumberSectionA = 0;
        this.lastAssignedSeatNumberSectionB = 0;
    }

    // Getter and setter methods
    public Long getTrainId() {
        return id;
    }

    public void setTrainId(Long id) {
        this.id = id;
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

    public int getSectionASeatCapacity() {
        return sectionASeatCapacity;
    }

    public void setSectionASeatCapacity(int sectionASeatCapacity) {
        this.sectionASeatCapacity = sectionASeatCapacity;
    }

    public int getSectionBSeatCapacity() {
        return sectionBSeatCapacity;
    }

    public void setSectionBSeatCapacity(int sectionBSeatCapacity) {
        this.sectionBSeatCapacity = sectionBSeatCapacity;
    }

    public int getLastAssignedSeatNumberSectionA() {
        return lastAssignedSeatNumberSectionA;
    }

    public void setLastAssignedSeatNumberSectionA(int lastAssignedSeatNumberSectionA) {
        this.lastAssignedSeatNumberSectionA = lastAssignedSeatNumberSectionA;
    }

    public int getLastAssignedSeatNumberSectionB() {
        return lastAssignedSeatNumberSectionB;
    }

    public void setLastAssignedSeatNumberSectionB(int lastAssignedSeatNumberSectionB) {
        this.lastAssignedSeatNumberSectionB = lastAssignedSeatNumberSectionB;
    }
}
