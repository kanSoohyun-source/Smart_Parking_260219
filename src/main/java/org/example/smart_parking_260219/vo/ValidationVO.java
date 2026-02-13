package org.example.smart_parking_260219.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationVO {
    private int no;
    private String stringOTP;
    private String email;
    private LocalDateTime expiryTime;  //만료시간
}
