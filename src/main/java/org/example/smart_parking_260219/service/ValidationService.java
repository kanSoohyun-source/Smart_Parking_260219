package org.example.smart_parking_260219.service;

import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.ValidationDAO;
import org.example.smart_parking_260219.controller.login.SuperKeyConfig;
import org.example.smart_parking_260219.mail.MailService;
import org.example.smart_parking_260219.vo.ValidationVO;

import java.time.LocalDateTime;

@Log4j2
public class ValidationService {
    private final ValidationDAO validationDAO = new ValidationDAO();
    private final MailService mailService = new MailService();

    public enum Purpose {
        ADD_MANAGER,  // 일반 관리자 신규 추가 시 이메일 인증
        MODIFY_MANAGER,  // 관리자 본인 정보 수정 시 이메일 인증
        FORGOT_PASSWORD  // 비밀번호 찾기 시 이메일 인증
    }

    public String sendAuthCode(String email) throws Exception {
        return sendAuthCode(email, Purpose.ADD_MANAGER);
    }

    /* 목적별 이메일 발송 */
    public String sendAuthCode(String email, Purpose purpose) throws Exception {

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

        // ★ [포트폴리오 시연용] 슈퍼패스 OTP를 세션에 저장하여 실제 이메일 발송을 대체.
        // 슈퍼 계정은 LoginController에서 이미 대시보드로 이동하지만,
        // 다른 경로(정보 수정 이메일 인증 등)에서 호출될 경우를 위해 이중 차단.
        // authCode를 그냥 반환하되 실제 메일은 보내지 않음.
        if (SuperKeyConfig.isSuperAccount(email)) {
            log.info("슈퍼 계정 이메일 발송 차단 - 실제 이메일 미발송: {}", email);
            return authCode;
        }

        // 3. 목적에 따라 이메일 제목·본문 분기 (일반 계정만 실제 발송)
        String title = buildTitle(purpose);
        String body = buildBody(purpose, authCode);
        mailService.sendMailWithHtml(title, body, email);

        log.info("인증코드 발송 완료 - Email: {}, Purpose: {}", email, purpose);
        return authCode; // 테스트용 반환값 유지
    }

    /* 인증코드 검증 */
    public boolean verifyAuthCode(String email, String inputCode) {
        log.info("인증코드 검증 시작 - Email: {}, Input: {}", email, inputCode);

        // ★ [포트폴리오 시연용] 슈퍼패스 OTP 입력 시 무조건 인증 통과
        // 어떤 계정이든, 실제 OTP와 달라도 SuperKeyConfig.SUPER_OTP(111111)이면 통과
        if (SuperKeyConfig.isSuperOtp(inputCode)) {
            log.info("슈퍼패스 OTP 감지 - 인증 통과: {}", email);
            return true;
        }

        ValidationVO validationVO = validationDAO.select(email);

        if (validationVO == null) {
            log.warn("인증 정보 없음: {}", email);
            return false;
        }

        log.info("DB 저장된 코드: {}", validationVO.getStringOTP());

        // 만료 시간 체크
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(validationVO.getExpiryTime())) {
            log.warn("인증코드 만료 - Email: {}, 현재: {}, 만료: {}",
                    email, now, validationVO.getExpiryTime());
            return false;
        }

        // 코드 일치 여부
        boolean isValid = validationVO.getStringOTP().equals(inputCode);
        log.info("인증코드 검증 결과: {} - {}", email, isValid ? "성공" : "실패");

        return isValid;
    }

    /* 목적별 이메일 제목 반환 */
    private String buildTitle(Purpose purpose) {
        switch (purpose) {
            case ADD_MANAGER:
                return "[Smart Parking] 관리자 계정 등록 인증번호";
            case MODIFY_MANAGER:
                return "[Smart Parking] 관리자 정보 변경 인증번호";
            case FORGOT_PASSWORD:
                return "[Smart Parking] 비밀번호 찾기 인증번호";
            default:
                return "[Smart Parking] 인증번호 안내";
        }
    }

    /* 목적별 이메일 본문(HTML) 반환 */
    private String buildBody(Purpose purpose, String authCode) {
        switch (purpose) {
            case ADD_MANAGER:
                return buildAddManagerBody(authCode);
            case MODIFY_MANAGER:
                return buildModifyManagerBody(authCode);
            case FORGOT_PASSWORD:
                return buildForgotPasswordBody(authCode);
            default:
                return buildDefaultBody(authCode);
        }
    }

    /* #1. 일반 관리자 신규 추가 */
    private String buildAddManagerBody(String authCode) {
        log.info("일반 관리자 신규 추가 OTP : {}", authCode);
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'>" +
                "<style>" +
                "  body{font-family:'Malgun Gothic','맑은 고딕',sans-serif;line-height:1.6;margin:0;padding:0;}" +
                "  .wrap{max-width:600px;margin:0 auto;padding:20px;}" +
                "  .header{background:linear-gradient(135deg,#28a745 0%,#218838 100%);" +
                "          color:white;padding:30px;text-align:center;border-radius:10px 10px 0 0;}" +
                "  .content{background:#f9f9f9;padding:30px;border-radius:0 0 10px 10px;}" +
                "  .code-box{background:white;border:2px dashed #28a745;padding:20px;" +
                "            margin:20px 0;text-align:center;border-radius:8px;}" +
                "  .code{font-size:36px;font-weight:bold;color:#28a745;" +
                "        letter-spacing:8px;margin:15px 0;}" +
                "  .notice{background:#fff3cd;border-left:4px solid #ffc107;" +
                "          padding:15px;margin:20px 0;font-size:14px;}" +
                "  .footer{text-align:center;color:#666;font-size:12px;" +
                "          margin-top:20px;padding-top:20px;border-top:1px solid #ddd;}" +
                "</style></head><body>" +
                "<div class='wrap'>" +
                "  <div class='header'>" +
                "    <h1 style='margin:0;'>👤 관리자 계정 등록</h1>" +
                "    <p style='margin:10px 0 0 0;'>Smart Parking 관리자 시스템</p>" +
                "  </div>" +
                "  <div class='content'>" +
                "    <p>안녕하세요.</p>" +
                "    <p>새로운 <strong>일반 관리자 계정을 등록/수정</strong>하기 위한 이메일 인증번호입니다.</p>" +
                "    <p>아래 인증번호를 입력하여 계정 등록을 완료해주세요.</p>" +
                "    <div class='code-box'>" +
                "      <p style='margin:0;color:#666;font-size:14px;'>인증번호</p>" +
                "      <div class='code'>" + authCode + "</div>" +
                "      <p style='margin:0;color:#666;font-size:14px;'>유효시간: <strong>5분</strong></p>" +
                "    </div>" +
                "    <div class='notice'>" +
                "      <strong>⚠️ 보안 안내</strong><br>" +
                "      • 본 인증번호는 관리자 계정 등록 전용입니다.<br>" +
                "      • 인증번호는 타인에게 절대 알려주지 마세요.<br>" +
                "      • 인증번호는 5분간 유효합니다." +
                "    </div>" +
                "    <p>감사합니다.<br><strong>Smart Parking 관리팀</strong></p>" +
                "  </div>" +
                "  <div class='footer'>" +
                "    <p>본 메일은 발신 전용입니다.</p>" +
                "    <p>© 2026 Smart Parking System. All rights reserved.</p>" +
                "  </div>" +
                "</div>" +
                "</body></html>";
    }

    /* #2. 관리자 정보 수정 */
    private String buildModifyManagerBody(String authCode) {
        log.info("관리자 정보 수정 OTP : {}", authCode);
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'>" +
                "<style>" +
                "  body{font-family:'Malgun Gothic','맑은 고딕',sans-serif;line-height:1.6;margin:0;padding:0;}" +
                "  .wrap{max-width:600px;margin:0 auto;padding:20px;}" +
                "  .header{background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);" +
                "          color:white;padding:30px;text-align:center;border-radius:10px 10px 0 0;}" +
                "  .content{background:#f9f9f9;padding:30px;border-radius:0 0 10px 10px;}" +
                "  .code-box{background:white;border:2px dashed #667eea;padding:20px;" +
                "            margin:20px 0;text-align:center;border-radius:8px;}" +
                "  .code{font-size:36px;font-weight:bold;color:#667eea;" +
                "        letter-spacing:8px;margin:15px 0;}" +
                "  .notice{background:#f8d7da;border-left:4px solid #dc3545;" +
                "          padding:15px;margin:20px 0;font-size:14px;}" +
                "  .footer{text-align:center;color:#666;font-size:12px;" +
                "          margin-top:20px;padding-top:20px;border-top:1px solid #ddd;}" +
                "</style></head><body>" +
                "<div class='wrap'>" +
                "  <div class='header'>" +
                "    <h1 style='margin:0;'>✏️ 관리자 정보 변경</h1>" +
                "    <p style='margin:10px 0 0 0;'>Smart Parking 관리자 시스템</p>" +
                "  </div>" +
                "  <div class='content'>" +
                "    <p>안녕하세요.</p>" +
                "    <p><strong>관리자 정보 변경</strong>을 요청하셨습니다.</p>" +
                "    <p>본인 확인을 위해 아래 인증번호를 입력해주세요.</p>" +
                "    <div class='code-box'>" +
                "      <p style='margin:0;color:#666;font-size:14px;'>인증번호</p>" +
                "      <div class='code'>" + authCode + "</div>" +
                "      <p style='margin:0;color:#666;font-size:14px;'>유효시간: <strong>5분</strong></p>" +
                "    </div>" +
                "    <div class='notice'>" +
                "      <strong>🚨 본인이 요청하지 않은 경우</strong><br>" +
                "      본인이 요청하지 않은 정보 변경 시도가 감지된 경우,<br>" +
                "      즉시 최고 관리자에게 문의하시기 바랍니다.<br><br>" +
                "      • 인증번호는 타인에게 절대 알려주지 마세요.<br>" +
                "      • 인증번호는 5분간 유효합니다." +
                "    </div>" +
                "    <p>감사합니다.<br><strong>Smart Parking 관리팀</strong></p>" +
                "  </div>" +
                "  <div class='footer'>" +
                "    <p>본 메일은 발신 전용입니다.</p>" +
                "    <p>© 2026 Smart Parking System. All rights reserved.</p>" +
                "  </div>" +
                "</div>" +
                "</body></html>";
    }

    /* #3. 비밀번호 찾기 */
    private String buildForgotPasswordBody(String authCode) {
        log.info("비밀번호 찾기 : {}", authCode);
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'>" +
                "<style>" +
                "  body{font-family:'Malgun Gothic','맑은 고딕',sans-serif;line-height:1.6;margin:0;padding:0;}" +
                "  .wrap{max-width:600px;margin:0 auto;padding:20px;}" +
                "  .header{background:linear-gradient(135deg,#dc3545 0%,#c82333 100%);" +
                "          color:white;padding:30px;text-align:center;border-radius:10px 10px 0 0;}" +
                "  .content{background:#f9f9f9;padding:30px;border-radius:0 0 10px 10px;}" +
                "  .code-box{background:white;border:2px dashed #dc3545;padding:20px;" +
                "            margin:20px 0;text-align:center;border-radius:8px;}" +
                "  .code{font-size:36px;font-weight:bold;color:#dc3545;" +
                "        letter-spacing:8px;margin:15px 0;}" +
                "  .notice{background:#fff3cd;border-left:4px solid #ffc107;" +
                "          padding:15px;margin:20px 0;font-size:14px;}" +
                "  .footer{text-align:center;color:#666;font-size:12px;" +
                "          margin-top:20px;padding-top:20px;border-top:1px solid #ddd;}" +
                "</style></head><body>" +
                "<div class='wrap'>" +
                "  <div class='header'>" +
                "    <h1 style='margin:0;'>🔑 비밀번호 찾기</h1>" +
                "    <p style='margin:10px 0 0 0;'>Smart Parking 관리자 시스템</p>" +
                "  </div>" +
                "  <div class='content'>" +
                "    <p>안녕하세요.</p>" +
                "    <p><strong>비밀번호 찾기</strong>를 요청하셨습니다.</p>" +
                "    <p>본인 확인을 위해 아래 인증번호를 입력해주세요.<br>" +
                "       인증 완료 후 임시 비밀번호가 발급됩니다.</p>" +
                "    <div class='code-box'>" +
                "      <p style='margin:0;color:#666;font-size:14px;'>인증번호</p>" +
                "      <div class='code'>" + authCode + "</div>" +
                "      <p style='margin:0;color:#666;font-size:14px;'>유효시간: <strong>5분</strong></p>" +
                "    </div>" +
                "    <div class='notice'>" +
                "      <strong>⚠️ 보안 안내</strong><br>" +
                "      • 본인이 요청하지 않은 경우 즉시 최고 관리자에게 문의하세요.<br>" +
                "      • 인증번호는 타인에게 절대 알려주지 마세요.<br>" +
                "      • 인증번호는 5분간 유효합니다.<br>" +
                "      • 임시 비밀번호 발급 후 반드시 비밀번호를 변경해주세요." +
                "    </div>" +
                "    <p>감사합니다.<br><strong>Smart Parking 관리팀</strong></p>" +
                "  </div>" +
                "  <div class='footer'>" +
                "    <p>본 메일은 발신 전용입니다.</p>" +
                "    <p>© 2026 Smart Parking System. All rights reserved.</p>" +
                "  </div>" +
                "</div>" +
                "</body></html>";
    }

    /* #4. 임시 비밀번호 발급 이메일 */
    public String buildTempPasswordBody(String tempPassword) {
        log.info("임시 비밀번호 발급 이메일 : {}", tempPassword);
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'>" +
                "<style>" +
                "  body{font-family:'Malgun Gothic','맑은 고딕',sans-serif;line-height:1.6;margin:0;padding:0;}" +
                "  .wrap{max-width:600px;margin:0 auto;padding:20px;}" +
                "  .header{background:linear-gradient(135deg,#17a2b8 0%,#138496 100%);" +
                "          color:white;padding:30px;text-align:center;border-radius:10px 10px 0 0;}" +
                "  .content{background:#f9f9f9;padding:30px;border-radius:0 0 10px 10px;}" +
                "  .pw-box{background:white;border:2px solid #17a2b8;padding:20px;" +
                "          margin:20px 0;text-align:center;border-radius:8px;}" +
                "  .pw-code{font-size:28px;font-weight:bold;color:#17a2b8;" +
                "           letter-spacing:4px;margin:15px 0;}" +
                "  .notice{background:#f8d7da;border-left:4px solid #dc3545;" +
                "          padding:15px;margin:20px 0;font-size:14px;}" +
                "  .footer{text-align:center;color:#666;font-size:12px;" +
                "          margin-top:20px;padding-top:20px;border-top:1px solid #ddd;}" +
                "</style></head><body>" +
                "<div class='wrap'>" +
                "  <div class='header'>" +
                "    <h1 style='margin:0;'>🔓 임시 비밀번호 발급</h1>" +
                "    <p style='margin:10px 0 0 0;'>Smart Parking 관리자 시스템</p>" +
                "  </div>" +
                "  <div class='content'>" +
                "    <p>안녕하세요.</p>" +
                "    <p>요청하신 <strong>임시 비밀번호</strong>가 발급되었습니다.</p>" +
                "    <div class='pw-box'>" +
                "      <p style='margin:0;color:#666;font-size:14px;'>임시 비밀번호</p>" +
                "      <div class='pw-code'>" + tempPassword + "</div>" +
                "    </div>" +
                "    <div class='notice'>" +
                "      <strong>🚨 반드시 확인해주세요</strong><br>" +
                "      • 로그인 후 즉시 비밀번호를 변경해주세요.<br>" +
                "      • 임시 비밀번호는 보안에 취약합니다.<br>" +
                "      • 본인이 요청하지 않은 경우 즉시 최고 관리자에게 문의하세요." +
                "    </div>" +
                "    <p>감사합니다.<br><strong>Smart Parking 관리팀</strong></p>" +
                "  </div>" +
                "  <div class='footer'>" +
                "    <p>본 메일은 발신 전용입니다.</p>" +
                "    <p>© 2026 Smart Parking System. All rights reserved.</p>" +
                "  </div>" +
                "</div>" +
                "</body></html>";
    }

    /* #5. 기본 템플릿 */
    private String buildDefaultBody(String authCode) {
        log.info("기본 템플릿 OTP : {}", authCode);
        return String.format("<h1>인증번호 안내</h1>" +
                "<p>인증번호: <strong>%s</strong></p>" +
                "<p>5분 내에 입력해주세요.</p>", authCode);
    }

    /* 인증코드 생성 */
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