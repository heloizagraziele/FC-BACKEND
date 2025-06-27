package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.enuns.OrderStatus;
import br.com.gooddreams.api.enuns.PaymentMethod;

import java.time.Instant;

public record OrderUpdateDTO(
        Long id,
        Float totalPrice,
        OrderStatus status,
        PaymentMethod paymentMethod
) {
}
