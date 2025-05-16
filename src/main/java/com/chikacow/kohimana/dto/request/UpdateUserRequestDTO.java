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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import static com.chikacow.kohimana.util.enums.Gender.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateUserRequestDTO {
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @Email(message = "Must match email format")
    private String email;

    //@Pattern(regexp = "^\\d{10}$", message = "k: phone invalid format")
    @PhoneNumber(message = "Must match phone number format")
    private String phone;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    private String gender;

    //ko de Gender de exception dc xu ly o phan validation, not serialization
}
