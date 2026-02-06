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
        for (int i = 1; i <= 20; i++) {
            CarParkVO carParkVO = CarParkVO.builder()
                    .space("A" + (i < 10 ? "0" : "") + i)
                    .build();
            carParkDAO.insertCarPark(carParkVO);
        }

    }

    @Test
    public void selectCarParkTest() {
        List<CarParkVO> carParkVOList = carParkDAO.selectAllCarPark();

        for (CarParkVO carParkVO : carParkVOList) {
            log.info("carParks: {}", carParkVO);
        }
    }

    @Test
    public void updateInputCarCarParkTest() {
        String id = "A01";
        CarParkVO carParkVO = CarParkVO.builder()
                .carNum("가1111")
                .space(id)
                .build();
        carParkDAO.updateInputCarPark(carParkVO);
    }

    @Test
    public void updateOutputCarParkTest() {
        String id = "A01";
        CarParkVO carParkVO = CarParkVO.builder()
                .space(id)
                .build();
        carParkDAO.updateOutputCarPark(carParkVO);
    }

    @Test
    public void selectEmptyCarParkTest() {
        List<CarParkVO> carParkVOList = carParkDAO.selectEmptyCarPark();

        for (CarParkVO carParkVO : carParkVOList) {
            log.info("carParks: {}", carParkVO);
        }
    }
}