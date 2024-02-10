package com.company.service.impl;

import com.company.model.Train;
import com.company.repository.TrainRepository;
import com.company.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService {

    private final TrainRepository trainRepository;

    @Autowired
    public TrainServiceImpl(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Train saveTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public Train getTrainById(Long id) {
        return trainRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteTrainById(Long id) {
        trainRepository.deleteById(id);
    }

    @Override
    public Train updateSeatCapacities(Long id, Train train) {
        train.setTrainId(id);
        return trainRepository.save(train);
    }
}
