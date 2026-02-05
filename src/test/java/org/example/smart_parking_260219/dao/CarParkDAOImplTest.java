package org.example.smart_parking_260219.dao;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.CarParkVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@Log4j2
class CarParkDAOImplTest {
    private CarParkDAO carParkDAO;

    @BeforeEach
    protected void ready() {
        carParkDAO = new CarParkDAOImpl();
    }

    @Test
    public void insertCarPark() {
        CarParkVO carParkVO = CarParkVO.builder()
                .space("A1")
                .state(false)
                .build();
        carParkDAO.insertCarPark(carParkVO);
    }

    @Test
    public void selectCarParkTest() {
        List<CarParkVO> carParkVOList = carParkDAO.selectAllCarPark();

        for (CarParkVO carParkVO : carParkVOList) {
            log.info("carParks: {}", carParkVO);
        }
    }

    @Test
    public void updateCarParkTest() {
        String id = "A1";
        CarParkVO carParkVO = CarParkVO.builder()
                .carNum(1111)
                .phone("010-1111-1111")
                .space(id)
                .build();
        carParkDAO.updateCarPark(carParkVO);
    }

    @Test
    public void selectEmptyCarParkTest() {
        List<CarParkVO> carParkVOList = carParkDAO.selectEmptyCarPark();

        for (CarParkVO carParkVO : carParkVOList) {
            log.info("carParks: {}", carParkVO);
        }
    }
}