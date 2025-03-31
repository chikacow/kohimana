package com.chikacow.kohimana.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.io.Serializable;

@Getter
@Builder
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;

    private Long userId;

    private Long countryCode;

}
