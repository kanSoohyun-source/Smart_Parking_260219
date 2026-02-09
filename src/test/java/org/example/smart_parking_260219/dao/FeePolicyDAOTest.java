package org.example.smart_parking_260219.dao;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.FeePolicyVo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class FeePolicyDAOTest {
    private final FeePolicyDAO feePolicyDAO = FeePolicyDAO.getInstance();

    @Test
    public void insertPolicy()  {
        FeePolicyVo feePolicyVo = FeePolicyVo.builder()
                .gracePeriod(10)
                .defaultTime(10)
                .defaultFee(2000)
                .extraTime(30)
                .extraFee(1000)
                .lightDiscount(0.3)
                .disabledDiscount(0.5)
                .subscribedFee(100000)
                .maxDailyFee(15000)
                .isActive(true).build();
        feePolicyDAO.insertPolicy(feePolicyVo);
    }

    @Test
    void selectAllPolicies() {
        List<FeePolicyVo> list = feePolicyDAO.selectAllPolicies();

        log.info("총 정책 개수: {}", list.size());
        for (FeePolicyVo vo : list) {
            log.info("policy: {}", vo);
        }
    }

    @Test
    void selectOnePolicy() {
        int policyId = 4;

        FeePolicyVo vo = feePolicyDAO.selectOnePolicy(policyId);
        log.info("조회 결과: {}", vo);

    }


    @Test
    public void updatePolicyTest() {
        FeePolicyVo feePolicyVo = FeePolicyVo.builder()
                .policyId(1) // ★ 실제 DB에 존재하는 ID로
                .gracePeriod(5)
                .defaultTime(30)
                .defaultFee(3000)
                .extraTime(10)
                .extraFee(500)
                .lightDiscount(0.2)
                .disabledDiscount(0.5)
                .subscribedFee(120000)
                .maxDailyFee(20000)
                .isActive(true)
                .build();

        int updated = feePolicyDAO.updatePolicy(feePolicyVo);
        assertEquals(1, updated);
    }

    @Test
    public void deletePolicyTest() {
        int result = feePolicyDAO.deletePolicy(1);
        assertEquals(1, result);
    }



}

