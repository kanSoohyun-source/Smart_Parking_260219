package org.example.smart_parking_260219.util;

import org.mindrot.jbcrypt.BCrypt;
import lombok.extern.log4j.Log4j2;

/**
 * BCrypt를 사용한 안전한 비밀번호 암호화 유틸리티
 *
 * BCrypt 특징:
 * - 자동 솔트(salt) 생성 및 관리
 * - 레인보우 테이블 공격 방어
 * - 해싱 강도(work factor) 조절 가능
 * - 업계 표준 암호화 방식
 */
@Log4j2
public class PasswordUtil {

    /**
     * BCrypt 해싱 강도 (로그 라운드)
     * 기본값: 12
     *
     * 값이 클수록 보안은 강화되지만 처리 시간이 증가
     * - 10: 빠르지만 보안 약함 (개발/테스트용)
     * - 12: 권장값 (프로덕션)
     * - 14: 매우 강력 (고보안 환경)
     */
    private static final int WORK_FACTOR = 12;

    /**
     * 비밀번호를 BCrypt로 해싱
     *
     * @param plainPassword 평문 비밀번호
     * @return BCrypt 해시 문자열 (솔트 포함, 60자)
     *
     * 예시:
     * Input: "admin1234"
     * Output: "$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW"
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 null이거나 빈 문자열일 수 없습니다.");
        }

        try {
            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORK_FACTOR));
            log.debug("비밀번호 해싱 완료 (원본 길이: {}자)", plainPassword.length());
            return hashedPassword;

        } catch (Exception e) {
            log.error("비밀번호 해싱 중 오류 발생", e);
            throw new RuntimeException("비밀번호 암호화에 실패했습니다.", e);
        }
    }

    /**
     * 입력된 평문 비밀번호와 저장된 해시를 비교하여 검증
     *
     * @param plainPassword 사용자가 입력한 평문 비밀번호
     * @param hashedPassword DB에 저장된 BCrypt 해시
     * @return 일치하면 true, 아니면 false
     *
     * 예시:
     * checkPassword("admin1234", "$2a$12$R9h/...") → true
     * checkPassword("wrong123", "$2a$12$R9h/...") → false
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            log.warn("비밀번호 검증 실패: 입력된 비밀번호가 null 또는 빈 문자열");
            return false;
        }

        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            log.warn("비밀번호 검증 실패: 저장된 해시가 null 또는 빈 문자열");
            return false;
        }

        try {
            boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
            log.debug("비밀번호 검증 결과: {}", matches ? "일치" : "불일치");
            return matches;

        } catch (Exception e) {
            log.error("비밀번호 검증 중 오류 발생", e);
            return false;
        }
    }

    /**
     * 해시의 강도(work factor)를 확인
     *
     * @param hashedPassword BCrypt 해시
     * @return work factor 값 (10, 12, 14 등)
     */
    public static int getWorkFactor(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.length() < 7) {
            return -1;
        }

        try {
            // BCrypt 해시 형식: $2a$12$... (12가 work factor)
            String[] parts = hashedPassword.split("\\$");
            if (parts.length >= 3) {
                return Integer.parseInt(parts[2]);
            }
        } catch (Exception e) {
            log.warn("Work factor 추출 실패", e);
        }
        return -1;
    }

    /**
     * 테스트용 메인 메서드
     */
    public static void main(String[] args) {
        // 테스트 1: 비밀번호 해싱
        System.out.println("=== BCrypt 비밀번호 암호화 테스트 ===\n");

        String password = "admin1234";
//        String password = "test01";
        String hashed = hashPassword(password);

        System.out.println("평문 비밀번호: " + password);
        System.out.println("해시된 비밀번호: " + hashed);
        System.out.println("해시 길이: " + hashed.length() + "자");
        System.out.println("Work Factor: " + getWorkFactor(hashed));

        // 테스트 2: 비밀번호 검증
        System.out.println("\n=== 비밀번호 검증 테스트 ===\n");

        boolean correct = checkPassword("admin1234", hashed);
        boolean wrong = checkPassword("wrongpass", hashed);

        System.out.println("올바른 비밀번호 입력: " + correct + " ✅");
        System.out.println("잘못된 비밀번호 입력: " + wrong + " ❌");

        // 테스트 3: 같은 비밀번호라도 해시는 매번 다름 (솔트 자동 생성)
        System.out.println("\n=== 솔트 테스트 ===\n");

        String hash1 = hashPassword("test123");
        String hash2 = hashPassword("test123");
        String hash3 = hashPassword("test123");

        System.out.println("같은 비밀번호의 서로 다른 해시:");
        System.out.println("해시1: " + hash1);
        System.out.println("해시2: " + hash2);
        System.out.println("해시3: " + hash3);
        System.out.println("\n모두 다른 해시지만, 검증은 모두 통과:");
        System.out.println("hash1 검증: " + checkPassword("test123", hash1));
        System.out.println("hash2 검증: " + checkPassword("test123", hash2));
        System.out.println("hash3 검증: " + checkPassword("test123", hash3));
    }
}