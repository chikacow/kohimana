package com.chikacow.kohimana.mapper;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.model.Seat;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.util.enums.TableStatus;
import com.chikacow.kohimana.util.helper.SmoothData;

import java.util.function.Consumer;

public class SeatMapper implements DTOMapper<Seat>{
    public static Seat fromRequestDTOToEntity(Seat.SeatRequestDTO requestDTO) {
        return Seat.builder()
                .tableNo(requestDTO.getTableNo())
                .description(requestDTO.getDescription())
                .status(TableStatus.fromString(requestDTO.getStatus()))
                .build();
    }

    public static Seat.SeatResponseDTO fromEntityToResponseDTO(Seat seat) {
        return Seat.SeatResponseDTO.builder()
                .tableNo(seat.getTableNo())
                .description(seat.getDescription())
                .status(seat.getStatus())
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
