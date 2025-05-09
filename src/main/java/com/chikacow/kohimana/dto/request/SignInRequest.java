package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.util.enums.Platform;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SignInRequest implements Serializable {

    @NotBlank(message = "missing username")
    private String username;

    @NotBlank(message = "missing password")
    private String password;

    @NotNull
    private Platform platform;
}
