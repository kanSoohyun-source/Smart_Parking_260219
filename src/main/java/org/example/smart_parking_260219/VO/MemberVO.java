package org.example.smart_parking_260219.VO;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
    private Integer memberId;
    private String carNum;
    private Integer carType;
    private String name;
    private String phone;
    private boolean subscribed;
    private Date subStartDate;
    private Date subEndDate;
    private LocalDate createdAt;
    private boolean active;
}
