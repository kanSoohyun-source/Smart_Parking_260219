package org.example.smart_parking_260219.vo;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {

    private int memberId;
    private int subscriptionId;
    private String carNum;
    private int carType;
    private String name;
    private String phone;
    private boolean subscribed;
}
