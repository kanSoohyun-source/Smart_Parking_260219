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

    public static class SimpleAuthenticator extends Authenticator {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
    public static Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.username", username);
        props.put("mail.host", host);
        props.put("mail.trasport.protocol", "smtp");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }
}
