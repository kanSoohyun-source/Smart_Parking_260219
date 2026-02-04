package org.example.smart_parking_260219.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeDTO {
    private Integer subscriptionId;
    private String carNum;
    private String startDate;
    private String endDate;
    private boolean status;
    private LocalDate lastUpdate;

}
