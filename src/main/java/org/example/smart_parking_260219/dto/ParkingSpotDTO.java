package org.example.smart_parking_260219.dto;

import lombok.*;
import org.example.smart_parking_260219.vo.ParkingSpotVO;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpotDTO {
    private String spaceId;  // 주차 공간 번호(A1, B2...)
    private Boolean empty;  // 빈 공간 여부(True:비어있음, False:주차중)
    private String carNum;  // 현재 주차된 차량 번호(없으면 NULL)
    private LocalDateTime lastUpdate;  // 상태 변경일

    public ParkingSpotVO toVO() {
        ParkingSpotVO parkingSpotVO = ParkingSpotVO.builder()
                .spaceId(spaceId)
                .empty(empty)
                .carNum(carNum)
                .lastUpdate(lastUpdate)
                .build();
        return parkingSpotVO;
    }
}
