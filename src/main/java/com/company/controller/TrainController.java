package com.company.controller;


import com.company.model.Train;
import com.company.service.TrainService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trains")
public class TrainController {
    private final TrainService trainService;
    private final ModelMapper modelMapper;
    @Autowired
    public TrainController(TrainService trainService, ModelMapper modelMapper) {
        this.trainService = trainService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<Train> getAllTrains() {
        return trainService.getAllTrains();
    }
}
