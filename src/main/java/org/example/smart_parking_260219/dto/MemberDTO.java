package org.example.smart_parking_260219.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private LocalDateTime createDate;  // 가입일
    private boolean subscribed; // 월정액 회원 여부 (default false)

    private LocalDate subscribeStartDate;
    private LocalDate subscribeEndDate;

    // 차량 타입 변환 메서드
    public String getCarTypeName() {
        switch (this.carType) {
            case 2: return "월정액";
            case 3: return "경차";
            case 4: return "장애인";
            case 1:
            default: return "일반";  // ✅ default를 일반으로 변경
        }
    }

    // 가입일 시간 제외 년/월/일만 적용하는 메서드
    public String getCreateDateOnly() {
        if (this.createDate == null) return "";
        return this.createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
