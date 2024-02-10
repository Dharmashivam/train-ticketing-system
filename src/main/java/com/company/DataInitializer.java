package com.company;

import com.company.model.Train;
import com.company.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TrainRepository trainRepository;

    @Autowired
    public DataInitializer(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Train train1 = new Train("Delhi", "Bengaluru", 2, 2);
        Train train2 = new Train("Chennai", "Mumbai", 2, 2);

        trainRepository.save(train1);
        trainRepository.save(train2);
    }
}
