package org.example.smart_parking_260219.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeePolicyDTO {
    private int policyId;  // 요금 정책 고유 ID
    private int gracePeriod;  // 무료 회차 시간(분) - 이 시간 내 출차 시 0원
    private int defaultTime;  // 기본 요금 적용 시간(분) - 60분
    private int defaultFee;  // 기본 요금 - 2,000원
    private int extraTime;  // 추가 요금 단위 시간(분) - 30분
    private int extraFee;  // 추가 요금 - 1,000원
    private double lightDiscount;  // 경차 할인율(0.3 = 30%)
    private double disabledDiscount;  // 장애인 할인율(0.5 = 50%)
    private int subscribedFee;  // 월정액 가격 - 100,000원
    private int maxDailyFee;  // 일일 최대 요금 - 15,000원
    private boolean isActive;  // 현재 정책 활성화 여부
    private LocalDateTime modifyDate;  // 정책 수정일
}
