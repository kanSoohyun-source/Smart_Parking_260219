package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class ManagerServiceTest {
    private final ManagerService managerService = ManagerService.INSTANCE;

    @Test
    public void testAddManager() {
        // #0. 테스트용 데이터 생성
        ManagerDTO managerDTO = ManagerDTO.builder()
                .managerId("testAdmin")
                .managerName("테스트관리자")
                .password("1234")
                .email("test@parking.com")
                .build();

        log.info("등록 시도 데이터: {}", managerDTO);

        // 서비스 호출
        managerService.addManager(managerDTO);

        // 결과 확인 (DB에서 다시 조회)
//        ManagerDTO savedManager = managerService.getManager("testAdmin");
//        Assertions.assertNotNull(savedManager);
//        Assertions.assertEquals("testAdmin", savedManager.getManagerId());
    }

    @Test
    public void testIsAuthSuccess() {
        // #1. 정상 로그인 확인
        String id = "testAdmin";
        String pw = "1234";

        // 기존 데이터가 활성화(active=true) 되어 있어야 함
        boolean result = managerService.isAuth(id, pw);

        log.info("정상 로그인 확인 결과: {}", result);
        Assertions.assertTrue(result);  // 로그인 성공 = true
    }

    @Test
    public void testIsAuthFailPassword() {
        // #3. 비밀번호 불일치
        String id = "testAdmin";
        String pw = "wrong_password";

        boolean result = managerService.isAuth(id, pw);

        log.info("비밀번호 틀림 인증 결과: {}", result);
        Assertions.assertFalse(result);  // 비밀번호가 틀린 경우 = false
    }

    @Test
    public void testGetManager() {
        // #4. 존재하지 않는 계정 확인
        String id = "testAdmin";

//        ManagerDTO managerDTO = managerService.getManager(id);

//        log.info("존재하지 않는 아이디 인증 결과: {}", managerDTO);
//        Assertions.assertEquals(id, managerDTO.getManagerId());
    }

    @Test
    public void testIsAuthFailId() {
        // #5. 아이디가 존재하지 않는 경우 테스트
        String id = "non_existent_id";
        String pw = "1234";

        boolean result = managerService.isAuth(id, pw);

        log.info("존재하지 않는 아이디 인증 결과: {}", result);
        Assertions.assertFalse(result);
    }

    @Test
    public void testIsAuthDeactiveAccount() {
        // #6. 아이디/비번은 맞지만 비활성화(active=false) 상태인 경우
        String id = "Test01"; // DB에 active=false인 계정
        String pw = "1234";

        boolean result = managerService.isAuth(id, pw);

        log.info("비활성화 계정 인증 결과: " + result);
        Assertions.assertFalse(result, "비활성화 계정 = false(0)");
    }

    @Test
    public void testGetAllManagers() {
        log.info("=== 전체 관리자 목록 조회(Service) 테스트 ===");

        // 1. When: 서비스의 목록 조회 메서드 호출
        java.util.List<ManagerDTO> dtoList = managerService.getAllManagers();

        // 2. Then: 결과 검증
        // 리스트 객체 자체가 null이면 안 됨
        Assertions.assertNotNull(dtoList);

        // 최소한 하나 이상의 데이터가 들어있는지 확인 (테스트 DB 환경에 따라 조절)
        Assertions.assertTrue(dtoList.size() > 0, "조회된 관리자 DTO 목록이 비어있습니다.");

        // 3. Log: 데이터 확인
        dtoList.forEach(dto -> log.info("조회된 DTO: {}", dto));
        log.info("총 관리자 수: {}", dtoList.size());
    }
}