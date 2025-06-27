package br.com.gooddreams.api.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record OrderCreateRequestDTO(
        Long customerId,
        List<OrderItemRequestDTO> items,
        Long deliveryAddressId
) {}