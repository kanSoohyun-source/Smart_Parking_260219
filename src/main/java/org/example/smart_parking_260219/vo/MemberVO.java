package org.example.smart_parking_260219.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {

    private int memberId;
    private String carNum;
    private int carType;
    private String name;
    private String phone;
    private LocalDateTime createDate;
    private boolean subscribed;
}
