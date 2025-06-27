package br.com.gooddreams.api.services;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.entities.*;
import br.com.gooddreams.api.enuns.OrderStatus;
import br.com.gooddreams.api.enuns.PaymentMethod;
import br.com.gooddreams.api.mappers.OrderItemMapper;
import br.com.gooddreams.api.mappers.OrderMapper;
import br.com.gooddreams.api.repository.AddressRepository;
import br.com.gooddreams.api.repository.CustomerRepository;
import br.com.gooddreams.api.repository.OrderRepository;
import br.com.gooddreams.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    private AddressRepository addressRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderCreateRequestDTO dto) { // DTO de entrada é OrderCreateRequestDTO
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        Address deliveryAddress;

        Order order = new Order();
        order.setCustomer(customer);
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setPaymentMethod(null);

        // Buscar todos os produtos do DTO de uma vez para evitar N+1 no mapper
        List<Long> productIds = dto.items().stream()
                .map(item -> item.productId())
                .collect(Collectors.toList());
        List<Product> productsInOrder = productRepository.findAllById(productIds);
        // Opcional: Validar se todos os produtos foram encontrados aqui

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new java.util.ArrayList<>();

        // Itera sobre OrderItemRequestDTOs que estão dentro do OrderCreateRequestDTO
        for (OrderItemRequestDTO itemDto : dto.items()) { // <--- itemDto é do tipo OrderItemRequestDTO
            Product product = productsInOrder.stream()
                    .filter(p -> p.getId().equals(itemDto.productId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado no cache de produtos: " + itemDto.productId()));

            OrderItem orderItem = OrderItemMapper.toEntity(itemDto, order, product);
            totalAmount = totalAmount.add(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        System.out.println("OrderService: Ordem salva com ID: " + savedOrder.getId());
        return OrderMapper.toDTO(savedOrder);
    }

    // Listar todos os pedidos
    @Transactional
    public List<OrderResponseDTO> list() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDTO) // <--- Use o OrderMapper
                .toList();
    }

    // Buscar pedido por ID
    @Transactional
    public OrderResponseDTO show(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Pedido com o id " + id + " não foi encontrado.")
        );
        return OrderMapper.toDTO(order); // <--- Use o OrderMapper
    }

    // Atualizar pedido
    @Transactional
    public OrderResponseDTO update(Long id, OrderUpdateDTO dto) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Pedido não encontrado com ID: " + id)
        );

        if (dto.status() != null) {
            order.setStatus(dto.status());
        }
        if (dto.paymentMethod() != null) {
            order.setPaymentMethod(dto.paymentMethod());
        }

        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toDTO(updatedOrder); // <--- Use o OrderMapper
    }

    // Deletar pedido
    @Transactional
    public void destroy(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Pedido com o id " + id + " não foi encontrado.")
        );
        orderRepository.delete(order);
    }
}
