package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.util.enums.Role;
import com.chikacow.kohimana.validator.GenderSubset;
import com.chikacow.kohimana.validator.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserRequestDTO implements Serializable {

    @NotBlank(message = "firstname must not be blank")
    private String firstName;

    @NotNull(message = "lastname must not be null")
    private String lastName;

    @Email(message = "email must be in the right format")
    private String email;

    //@Pattern(regexp = "^\\d{10}$", message = "k: phone invalid format")
    @PhoneNumber
    private String phone;

    @NotNull(message = "birthday must not be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM-dd-yyyy")
    private Date dateOfBirth;


    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    private Gender gender;

    @NotNull
    private String username;

    @NotNull
    private String password;

    private List<String> roles;
}
