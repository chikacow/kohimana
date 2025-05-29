package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.util.enums.RoleEnum;
import com.chikacow.kohimana.validator.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import static com.chikacow.kohimana.util.enums.Gender.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRequestDTO implements UserDTO, Serializable {

    @NotBlank(message = "firstname must not be blank")
    private String firstName;

    @NotNull(message = "lastname must not be null")
    private String lastName;

    @Email(message = "email must be in the right format")
    private String email;

    //@Pattern(regexp = "^\\d{10}$", message = "k: phone invalid format")
    @PhoneNumber
    private String phone;

    @NotNull(message = "Birthday must not be blank")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;


    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    //@EnumSubset(enumClass = Gender.class)
    private String gender;


    @Username
    private String username;

    //@Password
    private String password;


//    @EnumSubset(enumClass = RoleEnum.class)
    private List<RoleEnum> roles;
}
