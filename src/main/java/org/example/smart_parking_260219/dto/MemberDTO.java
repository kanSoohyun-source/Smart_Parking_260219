package org.example.smart_parking_260219.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private int memberId; // 회원 고유 식별자, FK
    private String carNum; // 차량 번호
    private int carType; // 차량 유형 (1 -일반/ 2 - 월정액 / 3 - 경차 / 4 - 장애인)
    private String name; // 차량 운전자 이름
    private String phone; // 운전자 연락처
    private boolean subscribed; // 월정액 회원 여부 (default false)
    private LocalDate startDate; // 구독 시작 일자
    private LocalDate endDate; // 구독 만료 일자
    private LocalDateTime createDate;  // 가입일
    private int subscribedFee; // 기본값 10만원

    // 차량 타입 정의 메서드 사용
    public String CarTypeText() {
        switch (this.carType) {
            case 1: return "일반";
            case 2: return "월정액";
            case 3: return "경차";
            case 4: return "장애인";
            default: return "미지정";
        }
    }
}
