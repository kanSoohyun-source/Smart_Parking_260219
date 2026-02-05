package org.example.smart_parking_260219.vo;

import lombok.*;
import org.example.smart_parking_260219.dto.ParkingDTO;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParkingVO {
    int entryExitNo;            // 주차장 이용 인덱스
    String carNum;              // 차 번호
    String phone;               // 전화번호
    String space;               // 주차 구역 id
    LocalDateTime entryTime;    // 입차 시간
    LocalDateTime exitTime;     // 출차 시간
    int totalTime;              // 총 주차 시간
    int carType;                // 차량 종류(1 -> 일반, 2 -> 월정액, 3 -> 경차, 4 -> 장애인)
    boolean isPaid;             // 결제 여부

    public ParkingDTO toDTO() {
        ParkingDTO parkingDTO = ParkingDTO.builder()
                .entryExitNo(entryExitNo)
                .carNum(carNum)
                .phone(phone)
                .space(space)
                .entryTime(entryTime)
                .exitTime(exitTime)
                .totalTime(totalTime)
                .carType(carType)
                .isPaid(isPaid)
                .build();
        return parkingDTO;
    }
}
