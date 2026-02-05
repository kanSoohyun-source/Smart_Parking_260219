package org.example.smart_parking_260219.VO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class PaymentVO {
    private int paymentNo; // 주차 기록 고유 ID
    private int memberId; // 주차한 회원 ID
    private int policyId; // 요금 정책 고유 ID
    private String carNum; // 주차한 차량 번호
    private String paymentType;	// 결제 방식 (카드 / 월정액)
    private int calculatedFee; // 할인 적용 전 계산된 요금
    private int discountAmount; // 할인된 금액
    private int finalFee; // 실제 부과된 최종 요금
    private LocalDate salesDate; // 매출 집계 기준 날짜
}
