package com.chikacow.kohimana.dto.request;

import lombok.Getter;

@Getter
public class ResetPasswordDTO {
    private String secretKey;
    private String password;
    private String confirmPassword;
}
