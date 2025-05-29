package com.chikacow.kohimana.mapper;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.util.helper.SmoothData;

import java.util.function.Consumer;

public class UserMapper implements DTOMapper<User>{

    private UserMapper() {
        super();
    }

    public static User fromRequestDTOToEntity(UserRequestDTO requestDTO, String encodedPassword) {
        normalizeRequestDTO(requestDTO);
        return User.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .email(requestDTO.getEmail())
                .gender(Gender.fromString(requestDTO.getGender()))
                .dateOfBirth(requestDTO.getDateOfBirth())
                .phoneNumber(requestDTO.getPhone())
                .username(requestDTO.getUsername())
                .password(encodedPassword)
                .isActive(Boolean.TRUE)
                .build();
    }

    public static UserResponseDTO fromEntityToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .gender(user.getGender())
                .username(user.getUsername())
                .createdAt(user.getCreatedAt())
                .dateOfBirth(user.getDateOfBirth())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }



    private static void normalizeRequestDTO(UserDTO requestDTO) {

        requestDTO.setFirstName(SmoothData.smooth(requestDTO.getFirstName()));
        requestDTO.setLastName(SmoothData.smooth(requestDTO.getLastName()));

    }


    public static void updateUserFromRequestDTO(User user, UpdateUserRequestDTO requestDTO) {
        normalizeRequestDTO(requestDTO);

        applyIfNotNull(requestDTO.getFirstName(), user::setFirstName);
        applyIfNotNull(requestDTO.getLastName(), user::setLastName);
        applyIfNotNull(requestDTO.getEmail(), user::setEmail);
        applyIfNotNull(requestDTO.getGender(), gender -> user.setGender(Gender.fromString(gender)));
        applyIfNotNull(requestDTO.getDateOfBirth(), user::setDateOfBirth);
        applyIfNotNull(requestDTO.getPhone(), user::setPhoneNumber);

    }

    private static <T> void applyIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
