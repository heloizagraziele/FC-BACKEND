package br.com.gooddreams.api.services;

import br.com.gooddreams.api.dtos.OrderItemRequestDTO;
import br.com.gooddreams.api.dtos.OrderItemResponseDTO;
import br.com.gooddreams.api.entities.Order;
import br.com.gooddreams.api.entities.OrderItem;
import br.com.gooddreams.api.entities.Product;
import br.com.gooddreams.api.mappers.OrderItemMapper;
import br.com.gooddreams.api.repository.OrderItemRepository;
import br.com.gooddreams.api.repository.OrderRepository;
import br.com.gooddreams.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderItemResponseDTO create(Long orderId, OrderItemRequestDTO itemDto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada com ID: " + orderId));

        Product product = productRepository.findById(itemDto.productId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemDto.productId()));


        OrderItem orderItem = OrderItemMapper.toEntity(itemDto, order, product);


        OrderItem saved = orderItemRepository.save(orderItem);


        return OrderItemMapper.toDTO(saved);
    }

    @Transactional
    public List<OrderItemResponseDTO> list() {
        return orderItemRepository.findAll().stream()
                .map(OrderItemMapper::toDTO)
                .toList();
    }

    @Transactional
    public OrderItemResponseDTO show(long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item com o id " + id + " não foi encontrado."));
        return OrderItemMapper.toDTO(orderItem);
    }

    @Transactional
    public OrderItemResponseDTO update(Long id, OrderItemRequestDTO dto) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de pedido não encontrado com ID: " + id));

        if (dto.quantity() != null && dto.quantity() > 0) {
            orderItem.setQuantity(dto.quantity());
        }

        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);

        return OrderItemMapper.toDTO(updatedOrderItem);
    }

    @Transactional
    public void destroy(long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item com o id " + id + " não foi encontrado."));
        orderItemRepository.delete(orderItem);
    }
}
