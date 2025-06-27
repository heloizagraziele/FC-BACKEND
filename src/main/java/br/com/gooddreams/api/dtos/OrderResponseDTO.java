package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.enuns.OrderStatus; // Importe OrderStatus
import java.math.BigDecimal; // Importe BigDecimal
import java.time.Instant; // Importe Instant
import java.util.List; // Importe List

public record OrderResponseDTO(
        Long id,
        Long customerId,
        BigDecimal totalAmount,
        OrderStatus status,
        Instant createdAt,
        List<OrderItemResponseDTO> items
) {

}
