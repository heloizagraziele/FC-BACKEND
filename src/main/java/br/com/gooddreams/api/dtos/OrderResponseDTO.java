package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.enuns.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        Long customerId,
        AddressResponseDTO deliveryAddress,
        BigDecimal totalAmount,
        OrderStatus status,
        Instant createdAt,
        List<OrderItemResponseDTO> items
) {

}
