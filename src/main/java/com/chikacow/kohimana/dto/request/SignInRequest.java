package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.util.enums.Platform;
import com.chikacow.kohimana.validator.Password;
import com.chikacow.kohimana.validator.Username;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SignInRequest implements Serializable {

    @Username
    private String username;

    //@Password
    private String password;

    //@NotNull(message = "platform must not be null")
    private Platform platform;
}
