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
    public OrderItemResponseDTO create(Long orderId, OrderItemRequestDTO itemDto) { // Assinatura OK
        // 1. Buscar a Order e o Product pelo ID (usando os parâmetros orderId e itemDto.productId())
        Order order = orderRepository.findById(orderId) // <--- CORREÇÃO AQUI: Usa 'orderId' do parâmetro
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada com ID: " + orderId)); // <--- CORREÇÃO AQUI

        Product product = productRepository.findById(itemDto.productId()) // Usa itemDto.productId()
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemDto.productId()));

        // 2. Criar a entidade OrderItem usando o Mapper
        OrderItem orderItem = OrderItemMapper.toEntity(itemDto, order, product);

        // 3. Salvar o OrderItem
        OrderItem saved = orderItemRepository.save(orderItem);

        // 4. Retornar o DTO de resposta
        return OrderItemMapper.toDTO(saved);
    }

    @Transactional
    public List<OrderItemResponseDTO> list() {
        return orderItemRepository.findAll().stream()
                .map(OrderItemMapper::toDTO)
                .toList();
    }

    @Transactional
    public OrderItemResponseDTO show(long id) { // Assinatura OK
        OrderItem orderItem = orderItemRepository.findById(id) // <--- CORREÇÃO AQUI: Usa 'id' do parâmetro
                .orElseThrow(() -> new RuntimeException("Item com o id " + id + " não foi encontrado.")); // <--- CORREÇÃO AQUI
        return OrderItemMapper.toDTO(orderItem);
    }

    @Transactional
    public OrderItemResponseDTO update(Long id, OrderItemRequestDTO dto) { // Assinatura OK
        OrderItem orderItem = orderItemRepository.findById(id) // <--- CORREÇÃO AQUI: Usa 'id' do parâmetro
                .orElseThrow(() -> new RuntimeException("Item de pedido não encontrado com ID: " + id)); // <--- CORREÇÃO AQUI

        if (dto.quantity() != null && dto.quantity() > 0) {
            orderItem.setQuantity(dto.quantity());
        }

        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);

        return OrderItemMapper.toDTO(updatedOrderItem);
    }

    @Transactional
    public void destroy(long id) { // Assinatura OK
        OrderItem orderItem = orderItemRepository.findById(id) // <--- CORREÇÃO AQUI: Usa 'id' do parâmetro
                .orElseThrow(() -> new RuntimeException("Item com o id " + id + " não foi encontrado.")); // <--- CORREÇÃO AQUI
        orderItemRepository.delete(orderItem);
    }
}
