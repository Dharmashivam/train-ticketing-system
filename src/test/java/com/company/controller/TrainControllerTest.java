package com.company.controller;

import com.company.model.Train;
import com.company.service.TrainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class TrainControllerTest {

    @Mock
    private TrainService trainService;

    @InjectMocks
    private TrainController trainController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainController).build();
    }

    @Test
    void testGetAllTrains() throws Exception {
        // Create some sample trains
        List<Train> trains = new ArrayList<>();
        trains.add(new Train("From Location 1", "To Location 1", 100, 150));
        trains.add(new Train("From Location 2", "To Location 2", 120, 180));

        // Mock the behavior of trainService.getAllTrains() to return the sample trains
        when(trainService.getAllTrains()).thenReturn(trains);

        // Perform GET request to "/trains" endpoint
        mockMvc.perform(get("/trains")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verify the JSON response contains the correct data
                .andExpect(jsonPath("$[0].fromLocation").value("From Location 1"))
                .andExpect(jsonPath("$[0].toLocation").value("To Location 1"))
                .andExpect(jsonPath("$[0].sectionASeatCapacity").value(100))
                .andExpect(jsonPath("$[0].sectionBSeatCapacity").value(150))
                .andExpect(jsonPath("$[1].fromLocation").value("From Location 2"))
                .andExpect(jsonPath("$[1].toLocation").value("To Location 2"))
                .andExpect(jsonPath("$[1].sectionASeatCapacity").value(120))
                .andExpect(jsonPath("$[1].sectionBSeatCapacity").value(180));
    }
}
