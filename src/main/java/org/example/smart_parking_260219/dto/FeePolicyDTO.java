package org.example.smart_parking_260219.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.smart_parking_260219.vo.FeePolicyVo;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeePolicyDTO {
    private int policyId;           // policy_no (PK)
    private int gracePeriod;        // 무료 회차 시간(분)
    private int defaultTime;        // 기본 시간
    private int defaultFee;         // 기본 요금
    private int extraTime;          // 추가 시간
    private int extraFee;           // 추가 요금
    private double lightDiscount;    // 경차 할인율
    private double disabledDiscount;    // 장애인 할인율
    private int subscribedFee;         // 월정액 가격
    private int maxDailyFee;        // 일일 최대 금액 (max_daily_fee)
    private boolean isActive;        // 현재 정책 활성화 여부
    private LocalDateTime modifyDate;// 정책 수정일

    public FeePolicyVo toVo() {
        return FeePolicyVo.builder()
                .policyId(policyId)
                .gracePeriod(gracePeriod)
                .defaultTime(defaultTime)
                .defaultFee(defaultFee)
                .extraTime(extraTime)
                .extraFee(extraFee)
                .lightDiscount(lightDiscount)
                .disabledDiscount(disabledDiscount)
                .subscribedFee(subscribedFee)
                .maxDailyFee(maxDailyFee)
                .isActive(isActive)
                .modifyDate(modifyDate)
                .build();
    }
}
