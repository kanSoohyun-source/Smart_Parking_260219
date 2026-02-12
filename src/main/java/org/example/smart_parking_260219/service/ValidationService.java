package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.ValidationDAO;
import org.example.smart_parking_260219.mail.MailService;
import org.example.smart_parking_260219.vo.ValidationVO;

import java.time.LocalDateTime;

@Log4j2
public class ValidationService {
    private final ValidationDAO validationDAO = new ValidationDAO();
    private final MailService mailService = new MailService();

    // 인증코드 생성 및 이메일 발송
    public String sendAuthCode(String email) throws Exception {

        // 0. 기존 인증 정보 삭제 (재발송 시)
        validationDAO.deleteByEmail(email);

        // 1. 인증코드 생성
        String authCode = generateAuthCode();

        // 2. DB에 저장
        ValidationVO validationVO = ValidationVO.builder()
                .stringOTP(authCode)
                .email(email)
                .build();
        validationDAO.insert(validationVO);

        // 3. 이메일 발송
        String title = "회원가입 인증 번호입니다.";
        String body = String.format(
                "<h1>회원가입 인증 번호</h1><p>인증번호: <strong>%s</strong></p><p>10분 내에 입력해주세요.</p>",
                authCode
        );
        mailService.sendMailWithHtml(title, body, email);

        log.info("인증코드 발송 완료: " + email);
        return authCode; // 필요시 반환 (테스트용)
    }

    // 인증코드 검증
    public boolean verifyAuthCode(String email, String inputCode) {
        log.info("인증코드 검증 시작 - Email: " + email + ", Input: " + inputCode);
        ValidationVO validationVO = validationDAO.select(email);

        if (validationVO == null) {
            log.warn("인증 정보 없음: " + email);
            return false;
        }

        log.info("DB 저장된 코드: " + validationVO.getStringOTP());

        // 만료 시간 체크
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(validationVO.getExpiryTime())) {
            log.warn("인증코드 만료: " + email + ", 현재: " + now + ", 만료: " + validationVO.getExpiryTime());
            return false;
        }

        // 코드 일치 여부
        boolean isValid = validationVO.getStringOTP().equals(inputCode);
        log.info("인증코드 검증 결과: " + email + " - " + (isValid ? "성공" : "실패"));

        return isValid;
    }

    private String generateAuthCode() {
        int codeLength = 6;
        StringBuilder authCode = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int digit = (int) (Math.random() * 10);
            authCode.append(digit);
        }
        return authCode.toString();
    }
}
