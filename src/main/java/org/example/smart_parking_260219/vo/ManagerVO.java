package org.example.smart_parking_260219.vo;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerVO {
    private int managerNo;  // 관리자 시스템 내부 번호
    private String managerId;  // 로그인 아이디 (중복 불가)
    private String managerName;  // 관리자 이름
    private String password;  // 관리자 비밀번호 (해시 암호화)
    private String email;  // 관리자 이메일(2차 인증)
    private boolean active;  // 계정 활성화 여부 (default true)
    private String role;  // 권한 (ADMIN/NORMAL)
}
