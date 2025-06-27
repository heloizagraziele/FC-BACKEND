package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.OrderItemRequestDTO;
import br.com.gooddreams.api.dtos.OrderItemResponseDTO;
import br.com.gooddreams.api.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired; // @Autowired é menos recomendado que injeção via construtor
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/order-items") // URL base para OrderItems
public class OrderItemController {

    private final OrderItemService orderItemService; // <--- Injeção via construtor (melhor prática)

    // Injeção de dependência via construtor
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // Criar novo item de pedido
    // Este endpoint só faz sentido se você criar OrderItems DEPOIS de uma Order existente.
    // Ele precisa do ID da Order na URL.
    @PostMapping("/{orderId}") // <--- ROTA CORRIGIDA: Inclui o orderId na URL
    public ResponseEntity<OrderItemResponseDTO> create(
            @PathVariable("orderId") Long orderId, // <--- ADICIONADO: Recebe o ID da Order da URL
            @RequestBody OrderItemRequestDTO dto) {
        OrderItemResponseDTO response = orderItemService.create(orderId, dto); // <--- PASSA O orderId
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar todos os itens de pedido
    @GetMapping
    public ResponseEntity<List<OrderItemResponseDTO>> list() {
        List<OrderItemResponseDTO> items = orderItemService.list();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // Buscar item por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable long id) { // Note: 'long' pode ser 'Long' para consistência
        try {
            OrderItemResponseDTO item = orderItemService.show(id);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Atualizar item de pedido
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long id, // <--- ADICIONADO: Recebe o ID do item da URL
            @RequestBody OrderItemRequestDTO dto) { // <--- DTO de requisição para update também
        try {
            OrderItemResponseDTO updated = orderItemService.update(id, dto); // <--- PASSA O ID
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Deletar item de pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable long id) { // Note: 'long' pode ser 'Long' para consistência
        try {
            orderItemService.destroy(id);
            return new ResponseEntity<>("Item deletado com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}