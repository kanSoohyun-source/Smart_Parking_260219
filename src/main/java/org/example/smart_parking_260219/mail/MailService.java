package org.example.smart_parking_260219.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailService {
    private final Properties props;
    private final NaverEmailConfig.SimpleAuthenticator authenticator;

    public MailService() {
        this.props = NaverEmailConfig.getProperties();
        this.authenticator = new NaverEmailConfig.SimpleAuthenticator();
    }

    public void sendMail(String title, String body, String toEmail) {
        /* 메일 발송 : 단순 텍스트 (메일 제목, 메일 내용, 받는 사람) */
        // JavaMail 세션은 실제 네트워크 연결 세션과는 다름. 정보를 담고 있는 객체
        Session session = Session.getInstance(props, authenticator);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.username")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(title);
            message.setText(body);
            // Transport 클래스의 정적 send 메소드는 메시지를 보낸 후에 자동으로 연결을 종료
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMailWithHtml(String title, String body, String toEmail) {
        /* 메일 발송 : html 발송 (메일 제목, 메일 내용, 받는 사람) */
        // JavaMail 세션은 실제 네트워크 연결 세션과는 다름. 정보를 담고 있는 객체
        Session session = Session.getInstance(props, authenticator);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.username")));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(title);
             message.setText(body);
            // HTML 컨텐츠 설정
            message.setContent(body, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMailWithHtmlForAuth(String toEmail) {
        /* 메일 발송 : html 발송, 인증 번호 (메일 제목, 메일 내용, 받는 사람) */
        // JavaMail 세션은 실제 네트워크 연결 세션과는 다름. 정보를 담고 있는 객체
        Session session = Session.getInstance(props, authenticator);
        String title = "회원가입 인증 번호 입니다.";
        String body = "<h1>회원가입 인증 번호 입니다." + generateAuthCode() + "</h1><p>";

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.username")));

            message.setRecipients(Message.RecipientType.TO
                    , InternetAddress.parse(toEmail));
            message.setSubject(title);
            //message.setText(body);
            // HTML 컨텐츠 설정
            message.setContent(body,
                    "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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