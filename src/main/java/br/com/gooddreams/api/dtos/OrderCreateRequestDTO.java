package br.com.gooddreams.api.dtos;

import java.util.List;

public record OrderCreateRequestDTO(
        Long customerId,
        List<OrderItemRequestDTO> items,
        Long deliveryAddressId
) {}