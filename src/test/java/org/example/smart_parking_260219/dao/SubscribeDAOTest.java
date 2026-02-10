package org.example.smart_parking_260219.dao;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.SubscribeVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
class SubscribeDAOTest {
    private SubscribeDAO subscribeDAO;

    @BeforeEach
    public void ready() {
        subscribeDAO = SubscribeDAO.getInstance();
    }

    @Test
    public void insertTest() throws SQLException {
        SubscribeVO subscribeVO = SubscribeVO.builder()
                .carNum("1234")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .status(true)
                .paymentAmount(100000)
                .build();
        subscribeDAO.insertSubscribe(subscribeVO);
    }

    @Test
    public void dummy() throws SQLException {
        for (int i = 0; i < 8; i++) {
            SubscribeVO subscribeVO = SubscribeVO.builder()
                    .carNum("111" + (i + 1))
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .status(false)
                    .paymentAmount(10000 + i)
                    .lastUpdate(LocalDateTime.now())
                    .build();
            subscribeDAO.insertSubscribe(subscribeVO);
        }
    }

    @Test
    public void selectAllTest() throws SQLException {
        var subs = subscribeDAO.selectAllSubscribe();

        for (SubscribeVO subscribeVO : subs) {
            log.info(subscribeVO);
        }
    }

    @Test
    public void selectOne() throws SQLException {
        String carNum = "1234";

        SubscribeVO subscribeVO = subscribeDAO.selectOneSubscribe(carNum);
        log.info(subscribeVO);

        Assertions.assertNotNull(subscribeVO);
    }

    @Test
    public void update() throws SQLException {
        String carNum = "1234";

        SubscribeVO subscribeVO = SubscribeVO.builder()
                .carNum(carNum)
                .startDate(LocalDate.of(2024, 3, 1))  // 고정 날짜
                .endDate(LocalDate.of(2025, 3, 1))
                .status(true)
                .paymentAmount(200000)
                .build();

        subscribeDAO.updateSubscribe(subscribeVO);

        SubscribeVO result = subscribeDAO.selectOneSubscribe(carNum);

        if(subscribeVO.getStartDate().equals(result.getStartDate()) &&
                subscribeVO.getEndDate().equals(result.getEndDate()) &&
                subscribeVO.isStatus() == result.isStatus() &&
                subscribeVO.getPaymentAmount() == result.getPaymentAmount()) {
            log.info("변경 성공");
        }
    }

    @Test
    public void deleteTest() throws SQLException {
        String carNum = "1234";
        subscribeDAO.deletesubscribe(carNum);
    }
}