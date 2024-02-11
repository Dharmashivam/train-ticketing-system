package com.company;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TrainTicketingSystemApplicationTests {

    @Autowired
    private ApplicationContext context;
    @Test
    public void contextLoads() {
        assertNotNull(context);
    }
}
