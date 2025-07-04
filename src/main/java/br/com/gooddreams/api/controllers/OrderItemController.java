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
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;


    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<OrderItemResponseDTO> create(
            @PathVariable("orderId") Long orderId,
            @RequestBody OrderItemRequestDTO dto) {
        OrderItemResponseDTO response = orderItemService.create(orderId, dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderItemResponseDTO>> list() {
        List<OrderItemResponseDTO> items = orderItemService.list();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // Buscar item por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable long id) {
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
            @PathVariable("id") Long id,
            @RequestBody OrderItemRequestDTO dto) {
        try {
            OrderItemResponseDTO updated = orderItemService.update(id, dto);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Deletar item de pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable long id) {
        try {
            orderItemService.destroy(id);
            return new ResponseEntity<>("Item deletado com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}