package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.OrderCreateRequestDTO;
import br.com.gooddreams.api.dtos.OrderResponseDTO;
import br.com.gooddreams.api.dtos.OrderUpdateDTO;
import br.com.gooddreams.api.entities.Customer; // Importe Customer
import br.com.gooddreams.api.repository.CustomerRepository; // Importe CustomerRepository
import br.com.gooddreams.api.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // Importe Principal
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;

    public OrderController(OrderService orderService, CustomerRepository customerRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody OrderCreateRequestDTO orderCreateRequestDTO) {
        OrderResponseDTO response = orderService.createOrder(orderCreateRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> list() {
        List<OrderResponseDTO> orders = orderService.list();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomerId(@PathVariable Long customerId, Principal principal) {
        String authenticatedUserEmail = principal.getName();

        Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

        if (!authenticatedCustomer.getId().equals(customerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<OrderResponseDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            OrderResponseDTO order = orderService.show(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long id,
            @RequestBody OrderUpdateDTO orderUpdateDTO) {
        try {
            OrderResponseDTO updated = orderService.update(id, orderUpdateDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro interno ao atualizar pedido.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable Long id) {
        try {
            orderService.destroy(id);
            return new ResponseEntity<>("Pedido deletado com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}