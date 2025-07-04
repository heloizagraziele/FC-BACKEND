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
    private final AddressRepository addressRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderCreateRequestDTO dto) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        Address deliveryAddress = addressRepository.findById(dto.deliveryAddressId())
                .orElseThrow(() -> new RuntimeException("Endereço de entrega não encontrado."));

        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(deliveryAddress);
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);


        List<Long> productIds = dto.items().stream()
                .map(OrderItemRequestDTO::productId)
                .collect(Collectors.toList());
        List<Product> productsInOrder = productRepository.findAllById(productIds);


        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new java.util.ArrayList<>();


        for (OrderItemRequestDTO itemDto : dto.items()) {
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


    @Transactional
    public List<OrderResponseDTO> list() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDTO)
                .toList();
    }

    @Transactional
    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }



    @Transactional
    public OrderResponseDTO show(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Pedido com o id " + id + " não foi encontrado.")
        );
        return OrderMapper.toDTO(order);
    }


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
        return OrderMapper.toDTO(updatedOrder);
    }


    @Transactional
    public void destroy(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Pedido com o id " + id + " não foi encontrado.")
        );
        orderRepository.delete(order);
    }
}