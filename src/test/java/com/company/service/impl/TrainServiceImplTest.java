package com.company.service.impl;

import com.company.model.Train;
import com.company.repository.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainServiceImplTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainServiceImpl trainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTrain() {
        Train train = new Train();
        train.setTrainId(1L);

        when(trainRepository.save(train)).thenReturn(train);

        Train savedTrain = trainService.saveTrain(train);

        assertEquals(train, savedTrain);
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void testGetAllTrains() {
        List<Train> trainList = new ArrayList<>();
        trainList.add(new Train());
        trainList.add(new Train());

        when(trainRepository.findAll()).thenReturn(trainList);

        List<Train> retrievedTrains = trainService.getAllTrains();

        assertEquals(trainList.size(), retrievedTrains.size());
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void testGetTrainById() {
        Train train = new Train();
        train.setTrainId(1L);

        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));

        Train retrievedTrain = trainService.getTrainById(1L);

        assertEquals(train, retrievedTrain);
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteTrainById() {
        doNothing().when(trainRepository).deleteById(1L);

        trainService.deleteTrainById(1L);

        verify(trainRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateSeatCapacities() {
        Train train = new Train();
        train.setTrainId(1L);

        when(trainRepository.save(train)).thenReturn(train);

        Train updatedTrain = trainService.updateSeatCapacities(1L, train);

        assertEquals(train, updatedTrain);
        verify(trainRepository, times(1)).save(train);
    }
}
