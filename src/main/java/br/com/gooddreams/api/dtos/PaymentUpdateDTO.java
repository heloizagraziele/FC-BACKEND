package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.entities.Order;
import br.com.gooddreams.api.enuns.PaymentMethod;
import br.com.gooddreams.api.enuns.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentUpdateDTO(
        Long id,
        Order order,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        LocalDateTime dateTimePayment
) {
}
