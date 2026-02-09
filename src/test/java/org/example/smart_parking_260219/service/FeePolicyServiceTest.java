package org.example.smart_parking_260219.service;


import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.FeePolicyDTO;
import org.junit.jupiter.api.Test;

import java.util.List;


@Log4j2
class FeePolicyServiceTest {
    private FeePolicyService feePolicyService = FeePolicyService.getInstance();

    @Test
    public void addPolicy() {
        FeePolicyDTO dto = FeePolicyDTO.builder()
                .gracePeriod(10)
                .defaultTime(10)
                .defaultFee(2000)
                .extraTime(30)
                .extraFee(1000)
                .lightDiscount(0.3)
                .disabledDiscount(0.5)
                .subscribedFee(100000)
                .maxDailyFee(15000)
                .isActive(false)
                .build();

        feePolicyService.addPolicy(dto);
        log.info("정책 등록 완료");
    }

    @Test
    public void getPolicyList() {
        List<FeePolicyDTO> list = feePolicyService.getPolicyList();

        log.info("정책 목록 개수: {}", list.size());
        for (FeePolicyDTO dto : list) {
            log.info("policy: {}", dto);
        }
    }

    @Test
    public void getPolicy() {
        int policyId = 4;

        FeePolicyDTO dto = feePolicyService.getPolicy(policyId);
        log.info("정책 상세 조회 결과: {}", dto);
    }

    @Test
    public void modifyPolicy() {

        FeePolicyDTO dto = FeePolicyDTO.builder()
                .policyId(4)
                .gracePeriod(10)
                .defaultTime(20)
                .defaultFee(3000)
                .extraTime(10)
                .extraFee(500)
                .lightDiscount(0.2)
                .disabledDiscount(0.4)
                .subscribedFee(120000)
                .maxDailyFee(20000)
                .isActive(true)
                .build();

        int result = feePolicyService.modifyPolicy(dto);
        log.info("정책 수정 결과(result=1이면 성공): {}", result);
    }

    @Test
    public void removePolicy() {
        int policyId = 3;

        int result = feePolicyService.removePolicy(policyId);
        log.info("정책 삭제 결과(result=1이면 성공): {}", result);
    }





}