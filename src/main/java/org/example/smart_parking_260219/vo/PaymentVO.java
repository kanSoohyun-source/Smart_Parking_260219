package org.example.smart_parking_260219.vo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class PaymentVO {
    private int paymentId; // 주차 기록 고유 ID
    private int parkingId; // 주차 기록 ID
    private int policyId; // 요금 정책 고유 ID
    private String carNum; // 주차한 차량 번호
    private int paymentType;	// 결제 방식 (카드 / 월정액)
    private int calculatedFee; // 할인 적용 전 계산된 요금
    private int discountAmount; // 할인된 금액
    private int finalFee; // 실제 부과된 최종 요금
    private LocalDate paymentDate; // 매출 집계 기준 날짜
}
