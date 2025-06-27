package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.OrderItemRequestDTO;
import br.com.gooddreams.api.dtos.OrderItemResponseDTO;
import br.com.gooddreams.api.entities.Order;
import br.com.gooddreams.api.entities.OrderItem;
import br.com.gooddreams.api.entities.Product;

public class OrderItemMapper {


    public static OrderItem toEntity(OrderItemRequestDTO dto, Order order, Product product) {
        if (dto == null || order == null || product == null) {
            throw new IllegalArgumentException("Dados incompletos para mapear OrderItem. Requer DTO, Order e Product.");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.quantity());
        orderItem.setUnitPrice(product.getPrice());
        return orderItem;
    }


    public static OrderItemResponseDTO toDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        String productName = (orderItem.getProduct() != null) ? orderItem.getProduct().getName() : null;
        Long productId = (orderItem.getProduct() != null) ? orderItem.getProduct().getId() : null;
        return new OrderItemResponseDTO(
                orderItem.getId(), productId, productName, orderItem.getUnitPrice(), orderItem.getQuantity()
        );
    }
}


