package com.chikacow.kohimana.dto.response;

import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.validator.GenderSubset;
import com.chikacow.kohimana.validator.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.chikacow.kohimana.util.enums.Gender.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO implements Serializable {
    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Date dateOfBirth;

    private Gender gender;

    private String username;

    private String password;

    private Date createdAt;

}
