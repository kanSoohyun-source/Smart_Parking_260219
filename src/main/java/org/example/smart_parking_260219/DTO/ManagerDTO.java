package org.example.smart_parking_260219.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDTO {
    private Integer managerNo;
    private Integer managerId;
    private String managerName;
    private String password;
    private String email;
    private boolean active;
}
