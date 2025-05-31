package com.chikacow.kohimana.mapper;

import com.chikacow.kohimana.dto.request.PaymentRequestDTO;
import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.PaymentResponseDTO;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.model.Order;
import com.chikacow.kohimana.model.Payment;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.util.helper.SmoothData;

import java.util.function.Consumer;

public class PaymentMapper implements DTOMapper<Payment> {
    public static Payment fromRequestDTOToEntity(PaymentRequestDTO requestDTO, Order order) {

        return Payment.builder()
                .order(order)
                .paymentMethod(requestDTO.getPaymentMethod())
                .amount(requestDTO.getAmount())
                .description(requestDTO.getDescription())
                .currency(requestDTO.getCurrency())
                .charge(requestDTO.getAmount().subtract(order.getTotalAmount()))
                .build();
    }

    public static PaymentResponseDTO fromEntityToResponseDTO(Payment payment) {
        return PaymentResponseDTO.builder()
                .transactionId(payment.getTransactionId())
                .orderId(payment.getOrder().getId())
                .username(payment.getUser().getUsername())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .description(payment.getDescription())
                .currency(payment.getCurrency())
                .charge(payment.getCharge())
                .build();

    }



    private static void normalizeRequestDTO(UserDTO requestDTO) {

        requestDTO.setFirstName(SmoothData.smooth(requestDTO.getFirstName()));
        requestDTO.setLastName(SmoothData.smooth(requestDTO.getLastName()));

    }


    public static void updateEntityFromRequestDTO(User user, UpdateUserRequestDTO requestDTO) {
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
