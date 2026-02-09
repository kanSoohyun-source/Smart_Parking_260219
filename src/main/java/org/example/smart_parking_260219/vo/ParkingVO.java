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
    private int parkingId;  // 주차 기록 ID
    private int memberId;  // 회원 ID (비회원일 경우 NULL)
    private String spaceId;  // 주차 공간 ID (FK)
    private String carNum;  // 차량 번호 (스냅샷)
    private int carType;  // 차량 유형 (스냅샷)
    private LocalDateTime entryTime;  // 입차 히간
    private LocalDateTime exitTime;  // 출차 시간 (주차중이면 Null)
    private int totalTime;  // 주차 시간(분), default 0
    private boolean paid;  //정산 오나료 여부, default false

    public ParkingDTO toDTO() {
        ParkingDTO parkingDTO = ParkingDTO.builder()
                .parkingId(parkingId)
                .carNum(carNum)
                .memberId(memberId)
                .spaceId(spaceId)
                .entryTime(entryTime)
                .exitTime(exitTime)
                .totalTime(totalTime)
                .carType(carType)
                .paid(paid)
                .build();
        return parkingDTO;
    }
}
