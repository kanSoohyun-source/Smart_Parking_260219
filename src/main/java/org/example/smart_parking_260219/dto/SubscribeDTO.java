package org.example.smart_parking_260219.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeDTO {
    private int subscriptionId;  // 월정액 이력 고유 ID
    private int memberId;  // 구독한 회원 ID (FK)
    private LocalDate startDate;  // 월정액 시작일
    private LocalDate endDate;  // 월정액 종료일
    private boolean status;  // 월정액 유효 여부 (default true)
    private int paymentAmount;  // 결제 금액
    private LocalDateTime lastUpdate;  // 결제일
}
