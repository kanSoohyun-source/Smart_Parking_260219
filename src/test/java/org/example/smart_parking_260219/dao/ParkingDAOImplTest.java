package org.example.smart_parking_260219.dao;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.ParkingVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Log4j2
class ParkingDAOImplTest {
    private ParkingDAO parkingDAO;

    @BeforeEach
    protected void ready() {
        parkingDAO = new ParkingDAOImpl();
    }

    @Test
    public void insertParkingTest() {
        ParkingVO parkingVO = ParkingVO.builder()
                .memberId(1)
                .carNum("가1111")
                .spaceId("A1")
                .carType(1)
                .build();
        parkingDAO.insertParking(parkingVO);
    }

    @Test
    public void selectParkingTest() {
        String carNum = "1111";

        ParkingVO parkingVO = parkingDAO.selectParkingByLast4(carNum);
        Assertions.assertNotNull(parkingVO);
        log.info("parking: {}", parkingVO);
    }

    @Test
    public void updateParkingTest() {
        String num = "가1111";

        ParkingVO parkingVO = ParkingVO.builder()
                .carNum(num)
                .build();
        parkingDAO.updateParking(num);
    }

    @Test
    public void selectParkingByIdTest() {
        int id = 1;

        ParkingVO parkingVO = parkingDAO.selectParkingByParkingId(id);
        Assertions.assertNotNull(parkingVO);
        log.info("parking: {}", parkingVO);
    }
}