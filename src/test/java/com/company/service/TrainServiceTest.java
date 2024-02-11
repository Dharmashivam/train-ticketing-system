package com.company.service;

import com.company.model.Train;
import com.company.repository.TrainRepository;
import com.company.service.impl.TrainServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainServiceImpl trainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveTrain() {
        Train train = new Train("From Location", "To Location", 100, 150);

        when(trainRepository.save(train)).thenReturn(train);

        Train savedTrain = trainService.saveTrain(train);

        assertNotNull(savedTrain);
        assertEquals(train, savedTrain);
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void testGetAllTrains() {
        List<Train> trains = new ArrayList<>();
        trains.add(new Train("From Location 1", "To Location 1", 100, 150));
        trains.add(new Train("From Location 2", "To Location 2", 120, 180));

        when(trainRepository.findAll()).thenReturn(trains);

        List<Train> retrievedTrains = trainService.getAllTrains();

        assertNotNull(retrievedTrains);
        assertEquals(trains.size(), retrievedTrains.size());
        assertEquals(trains, retrievedTrains);
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void testGetTrainById() {
        Long trainId = 1L;
        Train train = new Train("From Location", "To Location", 100, 150);
        train.setTrainId(trainId);

        when(trainRepository.findById(trainId)).thenReturn(java.util.Optional.of(train));

        Train retrievedTrain = trainService.getTrainById(trainId);

        assertNotNull(retrievedTrain);
        assertEquals(train, retrievedTrain);
        verify(trainRepository, times(1)).findById(trainId);
    }

    @Test
    void testDeleteTrainById() {
        Long trainId = 1L;

        trainService.deleteTrainById(trainId);

        verify(trainRepository, times(1)).deleteById(trainId);
    }

    @Test
    void testUpdateSeatCapacities() {
        Long trainId = 1L;
        Train train = new Train("From Location", "To Location", 100, 150);
        train.setTrainId(trainId);

        when(trainRepository.save(train)).thenReturn(train);

        Train updatedTrain = trainService.updateSeatCapacities(trainId, train);

        assertNotNull(updatedTrain);
        assertEquals(train, updatedTrain);
        verify(trainRepository, times(1)).save(train);
    }
}
