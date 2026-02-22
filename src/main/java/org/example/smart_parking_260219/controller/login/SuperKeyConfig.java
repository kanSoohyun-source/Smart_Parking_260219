package org.example.smart_parking_260219.controller.login;

public class SuperKeyConfig {
    /*
    포트폴리오 시연용 -> super 계정
    - super 계정은 데이터베이스에 등록되어있는 ID, PW, email 인증 통과 시 SUPER_OTP를 통해 즉시 로그인 가능
    - super(role) : admin(관리자 메뉴 토글) + normal(내 정보 수정) = 동시 표시
     */

    /*
    SUPER_OTP
    - 실제 발송된 OTP와 달라도 입력값이 SUPER_OTP이면 인증 통과
    - 모든 계정에도 적용
     */

    private SuperKeyConfig() {}

    /* 슈퍼 계정 아이디 */
    // DB에 실제로 존재하는 계정
    public static final String SUPER_ID = "super";

    /* 슈퍼패스 OTP */
    public static final String SUPER_OTP = "369369";

    /* 세션에 저장할 슈퍼 역할값 (menu.jsp 분기 기준) */
    public static final String SUPER_ROLE = "SUPER";

    /* 슈퍼 계정 여부 확인 */
    public static boolean isSuperAccount(String managerId) {
        return SUPER_ID.equals(managerId);
    }

    /* 슈퍼패스 OTP 여부 확인 */
    public static boolean isSuperOtp(String inputOtp) {
        return SUPER_OTP.equals(inputOtp);
    }
}