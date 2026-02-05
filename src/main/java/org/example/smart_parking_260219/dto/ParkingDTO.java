package org.example.smart_parking_260219.dto;

import lombok.*;
import org.example.smart_parking_260219.vo.ParkingVO;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDTO {
    int parkingId;            // 주차장 이용 인덱스
    int memberId;              // 회원 ID
    String carNum;              // 차 번호
    String spaceId;               // 주차 구역 id
    LocalDateTime entryTime;    // 입차 시간
    LocalDateTime exitTime;     // 출차 시간
    int totalTime;              // 총 주차 시간
    int carType;                // 차량 종류(1, 2, 3... 숫자로 표현)
    boolean isPaid;             // 결제 여부

    public ParkingVO toVO() {
        ParkingVO parkingVO = ParkingVO.builder()
                .parkingId(parkingId)
                .memberId(memberId)
                .carNum(carNum)
                .spaceId(spaceId)
                .entryTime(entryTime)
                .exitTime(exitTime)
                .totalTime(totalTime)
                .carType(carType)
                .isPaid(isPaid)
                .build();
        return parkingVO;
    }
}
