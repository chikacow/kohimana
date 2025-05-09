package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.validator.GenderSubset;
import com.chikacow.kohimana.validator.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.chikacow.kohimana.util.enums.Gender.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateUserRequestDTO {
    private String firstName;

    private String lastName;

    private String email;

    //@Pattern(regexp = "^\\d{10}$", message = "k: phone invalid format")
    @PhoneNumber
    private String phone;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM-dd-yyyy")
    private Date dateOfBirth;

    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    private Gender gender;
}
