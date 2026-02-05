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
                .entryExitNo(1)
                .carNum("가1111")
                .phone("010-1111-1111")
                .space("A1")
                .carType(1)
                .build();
        parkingDAO.insertParking(parkingVO);
    }

    @Test
    public void selectParkingTest() {
        String id = "1111";

        ParkingVO parkingVO = parkingDAO.selectParking(id);
        Assertions.assertNull(parkingVO);
        log.info("parking: {}", parkingVO);
    }

    @Test
    public void updateParkingTest() {
        String id = "1111";

        ParkingVO parkingVO = ParkingVO.builder()
                .space(id)
                .build();
        parkingDAO.updateParking(parkingVO);
    }
}