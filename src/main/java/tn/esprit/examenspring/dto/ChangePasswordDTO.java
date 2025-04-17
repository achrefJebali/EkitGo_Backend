package tn.esprit.examenspring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {
    private String username;
    private String currentPassword;
    private String newPassword;
}
