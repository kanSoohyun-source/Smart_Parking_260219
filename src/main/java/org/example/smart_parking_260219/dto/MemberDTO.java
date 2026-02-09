package org.example.smart_parking_260219.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private int memberId;
    private String carNum;
    private int carType;
    private String name;
    private String phone;
    private LocalDateTime createDate;
    private boolean subscribed;
}
