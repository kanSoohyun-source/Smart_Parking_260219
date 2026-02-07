package org.example.smart_parking_260219.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private int memberId;
    private int subscriptionId;
    private String carNum;
    private int carType;
    private String name;
    private String phone;
    private boolean subscribed;
}
