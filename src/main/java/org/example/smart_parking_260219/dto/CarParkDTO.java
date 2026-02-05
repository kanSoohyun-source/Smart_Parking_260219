package org.example.smart_parking_260219.dto;

import lombok.*;
import org.example.smart_parking_260219.vo.CarParkVO;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CarParkDTO {
    String space;               // 주차 공간 구역
    boolean state;              // false: 빈자리, true: 사용중
    String carNum;                 // 차 번호
    LocalDateTime lastUpdate;   // 마지막으로 정보가 갱신된 시간

    public CarParkVO toVO() {
        CarParkVO carParkVO = CarParkVO.builder()
                .space(space)
                .state(state)
                .carNum(carNum)
                .lastUpdate(lastUpdate)
                .build();
        return carParkVO;
    }
}
