package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //Criar novo produto
    @PostMapping("create")
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductCreateDTO productCreateDTO) {
        ProductResponseDTO response = productService.create(productCreateDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar todos os produtos
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> list() {
        List<ProductResponseDTO> products = productService.list();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Buscar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable long id) {
        try {
            ProductResponseDTO product = productService.show(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Atualizar dados do produto
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ProductUpdateDTO productUpdateDTO) {
        try {
            ProductResponseDTO updated = productService.update(productUpdateDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Deletar um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable long id) {
        try {
            productService.destroy(id);
            return new ResponseEntity<>("Cliente deletado com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
