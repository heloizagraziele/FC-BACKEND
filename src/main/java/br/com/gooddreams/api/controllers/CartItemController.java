package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    // Criar um novo item no carrinho
    @PostMapping("create")
    public ResponseEntity<CartItemResponseDTO> create(@RequestBody CartItemCreateDTO cartItemCreateDTO) {
        CartItemResponseDTO response = cartItemService.create(cartItemCreateDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar todos os itens do carrinho
    @GetMapping
    public ResponseEntity<List<CartItemResponseDTO>> list() {
        List<CartItemResponseDTO> cartItems = cartItemService.list();
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    // Buscar itens do carrinho por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable long id) {
        try {
            CartItemResponseDTO cartItem = cartItemService.show(id);
            return new ResponseEntity<>(cartItem, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Atualizar dados do item do carrinho
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody CartItemUpdateDTO cartItemUpdateDTO) {
        try {
            CartItemResponseDTO updated = cartItemService.update(cartItemUpdateDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Deletar um item do carrinho
    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable long id) {
        try {
            cartItemService.destroy(id);
            return new ResponseEntity<>("Item deletado do carrinho com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
