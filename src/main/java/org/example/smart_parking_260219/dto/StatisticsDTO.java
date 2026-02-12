package org.example.smart_parking_260219.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class StatisticsDTO {
    private String label;  // x축 (날짜, 시간, 차종명 등)
    private long value;    // y축 (매출액, 차량 대수 등)
}
