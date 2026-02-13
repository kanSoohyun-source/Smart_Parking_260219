package org.example.smart_parking_260219.vo;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
    private int memberId; // 회원 고유 식별자, FK
    private String carNum; // 차량 번호
    private int carType; // 차량 유형 (1 -일반/ 2 - 월정액 / 3 - 경차 / 4 - 장애인)
    private String name; // 차량 운전자 이름
    private String phone; // 운전자 연락처
    private boolean subscribed; // 월정액 회원 여부 (default false)
    private LocalDate startDate; // 구독 시작 일자
    private LocalDate endDate; // 구독 만료 일자
    private LocalDateTime createDate;  // 가입일
    private int subscribedFee; // 월정액 비용 기본값 10만원
}
