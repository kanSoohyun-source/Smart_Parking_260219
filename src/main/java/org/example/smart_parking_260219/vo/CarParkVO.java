package org.example.smart_parking_260219.vo;

import lombok.*;
import org.example.smart_parking_260219.dto.CarParkDTO;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CarParkVO {
    String space;               // 주차 공간 구역
    boolean state;              // false: 빈자리, true: 사용중
    int carNum;                 // 차 번호
    String phone;               // 전화번호
    LocalDateTime lastUpdate;   // 마지막으로 정보가 갱신된 시간

    public CarParkDTO toDTO() {
        CarParkDTO carParkDTO = CarParkDTO.builder()
                .space(space)
                .state(state)
                .carNum(carNum)
                .phone(phone)
                .lastUpdate(lastUpdate)
                .build();
        return carParkDTO;
    }
}
