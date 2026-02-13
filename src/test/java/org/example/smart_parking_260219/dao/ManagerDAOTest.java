package org.example.smart_parking_260219.dao;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.vo.ManagerVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class ManagerDAOTest {
    private ManagerDAO managerDAO = ManagerDAO.getInstance();

    @Test
    public void insertManagerTest() {
        ManagerVO vo = ManagerVO.builder()
                .managerId("test03")
                .managerName("테스터03")
                .password("3333")
                .email("test03@naver.com")
                .build();
        managerDAO.insertManager(vo);
        log.info("관리자 등록 완료 {}", vo);
    }

    @Test
    public void selectOneTest() {
        String managerId = "test02";
        ManagerVO vo = managerDAO.selectOne(managerId);

        log.info("관리자 아이디 조회 결과: {}", vo);
        // 결과가 null이 아님을 검증
        Assertions.assertNotNull(vo);
        // 조회한 ID가 일치하는지 검증
        Assertions.assertEquals(managerId, vo.getManagerId());
    }

    @Test
    public void selectAllTest() {
        log.info("=== 전체 관리자 목록 조회 테스트 ===");

        // 1. When: 전체 목록 조회 메서드 실행
        java.util.List<ManagerVO> list = managerDAO.selectAll();

        // 2. Then: 결과 검증
        // 리스트가 null이 아니어야 함
        Assertions.assertNotNull(list, "조회된 리스트 객체가 null입니다.");

        // 데이터가 최소 하나 이상은 있어야 함 (테스트 전 데이터가 있다는 가정 하에)
        Assertions.assertFalse(list.isEmpty(), "조회된 관리자 목록이 비어있습니다.");

        // 3. Log: 결과 출력
        list.forEach(vo -> log.info("조회된 관리자: {}", vo));
        log.info("총 관리자 수: {}", list.size());
    }

    @Test
    public void updateActiveTest() {
        // 1. Given: 테스트 대상 아이디와 변경할 상태 설정
        String managerId = "test01";
        boolean targetStatus = false; // 비활성화 상태로 변경 시도

        // 2. When: 상태 변경 메서드 실행
        managerDAO.updateActive(targetStatus, managerId);

        // 3. Then: 검증을 위해 데이터를 다시 조회
        ManagerVO updatedVo = managerDAO.selectOne(managerId);

        // 결과 확인
        Assertions.assertNotNull(updatedVo, "조회된 관리자 정보가 없습니다.");
        Assertions.assertEquals(targetStatus, updatedVo.isActive(), "활성화 상태가 정상적으로 변경되지 않았습니다.");

        log.info("수정 완료 후 데이터: {}", updatedVo);
    }
}