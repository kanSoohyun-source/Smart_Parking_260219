package org.example.smart_parking_260219.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpotVO {
    private String spaceId;  // 주차 공간 번호(A1, B2...)
    private Boolean empty;  // 빈 공간 여부(True:비어있음, False:주차중)
    private String carNum;  // 현재 주차된 차량 번호(없으면 NULL)
    private LocalDateTime lastUpdate;  // 상태 변경일
}
