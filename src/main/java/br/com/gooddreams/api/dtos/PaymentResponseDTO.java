package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.entities.Order;
import br.com.gooddreams.api.enuns.PaymentMethod;
import br.com.gooddreams.api.enuns.PaymentStatus;

import java.time.Instant;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
        Long id,
        Order order,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        LocalDateTime dateTimePayment
) {
}
