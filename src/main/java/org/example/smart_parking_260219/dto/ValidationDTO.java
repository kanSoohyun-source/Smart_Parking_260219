package org.example.smart_parking_260219.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationDTO {
    private int no;
    private String stringOTP;
    private String email;
    private LocalDateTime expiryTime;  //만료시간
}
