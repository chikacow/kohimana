package com.chikacow.kohimana.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    @NotBlank(message = "missing secret key")
    private String secretKey;

    @NotBlank(message = "missing new password")
    private String password;

    @NotBlank(message = "please confirm your password")
    private String confirmPassword;
}
