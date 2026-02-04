package org.example.smart_parking_260219.VO;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerVO {
    private Integer managerNo;
    private Integer managerId;
    private String managerName;
    private String password;
    private String email;
    private boolean active;
}
