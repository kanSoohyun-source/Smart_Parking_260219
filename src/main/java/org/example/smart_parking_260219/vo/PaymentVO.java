package org.example.smart_parking_260219.vo;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVO {
    private int paymentId;  // 결제 ID
    private int parkingId;  // 주차 기록 ID (FK)
    private int policyId;  // 적용된 요금 정책 ID (FK)
    private String carNum; // 주차한 차량 번호
    private int paymentType;  // 결제 수단 (1:카드, 2:현금, 3:월정액)
    private int calculatedFee;  // 할인 전 요금
    private int discountAmount;  // 총 할인 금액
    private int finalFee;  // 최종 결제 금액
    private LocalDateTime paymentDate;  // 실제 결제 일시
}
