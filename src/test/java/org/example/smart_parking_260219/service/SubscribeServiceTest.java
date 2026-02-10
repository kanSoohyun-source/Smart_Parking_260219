package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.SubscribeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class SubscribeServiceTest {
    private SubscribeService subscribeService;

    @BeforeEach
    public void ready() {
        subscribeService = SubscribeService.INSTANCE;
    }

    @Test
    void addService() throws SQLException {
        SubscribeDTO subscribeDTO = SubscribeDTO.builder()
                .carNum("1234")
                .startDate(LocalDate.of(2026, 03, 02))
                .endDate(LocalDate.of(2026, 03, 03))
                .status(true)
                .paymentAmount(150000)
                .build();
        subscribeService.addSubscribe(subscribeDTO);
    }

    @Test
    void getAllTest() throws SQLException {
        var subscriber = subscribeService.getAllSubscribe();

        for (SubscribeDTO subscribeDTO : subscriber) {
            log.info(subscribeDTO);
        }
    }

    @Test
    void getOne() throws SQLException {
        String carNum = "1234";

        SubscribeDTO subscribeDTO = subscribeService.getOneSubscribe(carNum);
        log.info(subscribeDTO);
        Assertions.assertNotNull(subscribeDTO);

    }



}