package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.enuns.PaymentMethod;
import br.com.gooddreams.api.enuns.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentCreateDTO(
        Long orderId,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        LocalDateTime dateTimePayment) {

}
