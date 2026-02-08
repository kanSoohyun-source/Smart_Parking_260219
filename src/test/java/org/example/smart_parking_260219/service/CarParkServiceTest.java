package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.CarParkDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

@Log4j2
class CarParkServiceTest {
    private CarParkService carParkService = CarParkService.INSTANCE;

    @Test
    public void getAllCarParkTest() {
        List<CarParkDTO> carParkDTOList = carParkService.getAllCarPark();
        for (CarParkDTO carParkDTO : carParkDTOList) {
            log.info("carParkDTO : {}", carParkDTO);
        }
        log.info("목록 조회 완료");
    }

    @Test
    public void modifyInputCarParkTest() {
        carParkService.modifyInputCarPark(CarParkDTO.builder()
                        .space("A02")
                        .carNum("다3222")
                .build());
    }
    @Test
    public void modifyOutputCarParkTest() {
        carParkService.modifyOutputCarPark(CarParkDTO.builder()
                        .carNum("다3222")
                .build());
    }

    @Test
    public void getCarParkTest() {
        List<CarParkDTO> carParkDTOList = carParkService.getEmptyCarPark();
        for (CarParkDTO carParkDTO : carParkDTOList) {
            log.info("carParkDTO : {}", carParkDTO);
        }
        log.info("빈 공간 주차 목록 조회 완료");
    }
}