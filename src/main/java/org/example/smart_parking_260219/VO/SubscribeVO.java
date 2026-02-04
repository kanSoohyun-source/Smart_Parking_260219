package org.example.smart_parking_260219.VO;

import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeVO {
    private Integer subscriptionId;
    private String carNum;
    private String startDate;
    private String endDate;
    private boolean status;
    private LocalDate lastUpdate;
}
