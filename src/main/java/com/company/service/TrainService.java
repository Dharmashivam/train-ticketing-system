package com.company.service;

import com.company.model.Train;

import java.util.List;

public interface TrainService {
    Train saveTrain(Train train);
    List<Train> getAllTrains();
    Train getTrainById(Long id);
    void deleteTrainById(Long id);
    Train updateSeatCapacities(Long id, Train train);
}
