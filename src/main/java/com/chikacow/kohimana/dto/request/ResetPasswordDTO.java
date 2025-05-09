package com.chikacow.kohimana.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    private String secretKey;
    private String password;
    private String confirmPassword;
}
