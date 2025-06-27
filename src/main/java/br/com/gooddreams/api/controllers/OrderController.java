package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.OrderCreateRequestDTO;
import br.com.gooddreams.api.dtos.OrderResponseDTO;
import br.com.gooddreams.api.dtos.OrderUpdateDTO;
import br.com.gooddreams.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Criar um novo pedido (order)
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody OrderCreateRequestDTO orderCreateRequestDTO) { // <--- Nome do DTO e @Valid
        OrderResponseDTO response = orderService.createOrder(orderCreateRequestDTO); // <--- Chamar createOrder
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar todos os pedidos
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> list() {
        List<OrderResponseDTO> orders = orderService.list();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Buscar pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable long id) { // Note: 'long' pode ser 'Long' para consistência
        try {
            OrderResponseDTO order = orderService.show(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Atualizar dados do pedido
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

    // Deletar um pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable long id) {
        try {
            orderService.destroy(id);
            return new ResponseEntity<>("Pedido deletado com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
