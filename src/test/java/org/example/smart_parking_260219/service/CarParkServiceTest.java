package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.ParkingSpotDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

@Log4j2
class ParkingSpotServiceTest {
    private ParkingSpotService parkingSpotService = ParkingSpotService.INSTANCE;

    @Test
    public void getAllCarParkTest() {
        List<ParkingSpotDTO> parkingSpotDTOList = parkingSpotService.getAllParkingSpot();
        for (ParkingSpotDTO parkingSpotDTO : parkingSpotDTOList) {
            log.info("carParkDTO : {}", parkingSpotDTO);
        }
        log.info("목록 조회 완료");
    }

    @Test
    public void modifyInputCarParkTest() {
        parkingSpotService.modifyInputParkingSpot(ParkingSpotDTO.builder()
                        .spaceId("A02")
                        .carNum("다3222")
                .build());
    }
    @Test
    public void modifyOutputParkingSpotTest() {
        parkingSpotService.modifyOutputParkingSpot(ParkingSpotDTO.builder()
                        .carNum("다3222")
                .build());
    }

    @Test
    public void getParkingSpotTest() {
        List<ParkingSpotDTO> parkingSpotDTOList = parkingSpotService.getEmptyParkingSpot();
        for (ParkingSpotDTO parkingSpotDTO : parkingSpotDTOList) {
            log.info("carParkDTO : {}", parkingSpotDTO);
        }
        log.info("빈 공간 주차 목록 조회 완료");
    }
}