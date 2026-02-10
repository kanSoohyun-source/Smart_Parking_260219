package org.example.smart_parking_260219.service;


import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.FeePolicyDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Log4j2
class FeePolicyServiceTest {
    private FeePolicyService feePolicyService = FeePolicyService.getInstance();

    @Test
    public void addPolicy() {
        FeePolicyDTO dto = FeePolicyDTO.builder()
                .gracePeriod(20)
                .defaultTime(10)
                .defaultFee(1000)
                .extraTime(30)
                .extraFee(1000)
                .lightDiscount(0.3)
                .disabledDiscount(0.5)
                .subscribedFee(100000)
                .maxDailyFee(15000)
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
        FeePolicyDTO feePolicyDTO = feePolicyService.getPolicy();

        log.info("정책 상세 조회 결과: {}", feePolicyDTO);

        assertNotNull(feePolicyDTO);   // 활성 정책이 반드시 1개 존재해야 함
    }





}