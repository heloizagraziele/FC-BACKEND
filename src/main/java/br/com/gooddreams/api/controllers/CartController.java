package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    //Criar um novo carrinho
    @PostMapping("create")
    public ResponseEntity<CartResponseDTO> create(@RequestBody CartCreateDTO cartCreateDTO){
        CartResponseDTO response = cartService.create(cartCreateDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Buscar carrinho por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable long id) {
        try {
            CartResponseDTO cart = cartService.show(id);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Atualizar dados do carrinho
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody CartUpdateDTO cartUpdateDTO) {
        try {
            CartResponseDTO updated = cartService.update(cartUpdateDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Deletar um carrinho
    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable long id) {
        try {
            cartService.destroy(id);
            return new ResponseEntity<>("Carrinho deletado com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
