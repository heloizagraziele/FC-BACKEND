package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.entities.*;
import br.com.gooddreams.api.enuns.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity(OrderCreateRequestDTO dto, Customer customer, List<Product> products, Address deliveryAddress) {
        if (dto == null || customer == null || products == null) {

            return null;
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(null);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDTO itemDto : dto.items()) {
            Product product = products.stream()
                    .filter(p -> p.getId().equals(itemDto.productId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado no mapeamento com ID: " + itemDto.productId()));

            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    itemDto.quantity(),
                    product.getPrice()
            );
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity())));
        }

        order.setTotalAmount(totalAmount);

        orderItems.forEach(order::addOrderItem);

        return order;
    }


    public static OrderResponseDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemResponseDTO> itemResponses = new ArrayList<>();
        if (order.getItems() != null) {
            itemResponses = order.getItems().stream()
                    .map(OrderItemMapper::toDTO)
                    .collect(Collectors.toList());
        }

        return new OrderResponseDTO(
                order.getId(),
                order.getCustomer() != null ? order.getCustomer().getId() : null,
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                itemResponses
        );
    }
}
