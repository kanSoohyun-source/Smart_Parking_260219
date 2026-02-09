package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.ParkingDTO;
import org.junit.jupiter.api.Test;

@Log4j2
class ParkingServiceTest {
    private ParkingService parkingService = ParkingService.INSTANCE;

    @Test
    public void addParkingTest() {
        parkingService.addParking(ParkingDTO.builder()
                        .carNum("나2222")
                        .memberId(2)
                        .spaceId("A02")
                        .carType(2)
                        .build()
        );
        log.info("추가 완료");
    }

    @Test
    public void getParkingTest() {
        String carNum = "2222";
        ParkingDTO parkingDTO = parkingService.getParking(carNum);
        log.info("parkingDTO: {}", parkingDTO);
    }

    @Test
    public void modifyParkingTest() {
        String num = "나2222";
        ParkingDTO parkingDTO = ParkingDTO.builder()
                .carNum(num)
                .build();
        parkingService.modifyParking(parkingDTO);
    }

    @Test
    public void getByIdParkingTest() {
        int id = 1;
        ParkingDTO parkingDTO = parkingService.getByIdParking(id);
        log.info("parkingDTO: {}", parkingDTO);
    }
}