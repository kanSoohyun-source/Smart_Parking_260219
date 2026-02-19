package org.example.smart_parking_260219.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import java.util.Properties;

public class NaverEmailConfig {
    // smtp.port
    private final String port = "465";  //SSL포트
    private static final String host = "smtp.naver.com";
    // 보내는 이메일 계정
    private static final String username = "wndus6110@naver.com";
    // 비밀번호 (2차인증의 경우 애플리케이션 비밀번호)
    private static final String password = "1EV12JZMHMGR";

    // 내부 서버 연결 시 사용자 이름과 비밀번호를 전달하여 인증을 수행
    public static class SimpleAuthenticator extends Authenticator {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            // 서버에 인증 정보를 담은 객체를 반환
            return new PasswordAuthentication(username, password);
        }
    }

    // SMTP 연결에필요한 세부 프로토콜 및 보안 설정을 Properties 객체에 담아 반환
    public static Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.username", username);
        props.put("mail.host", host);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }
}
