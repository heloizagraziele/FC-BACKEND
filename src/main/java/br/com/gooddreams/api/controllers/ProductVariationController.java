package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.services.ProductVariationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product_variations")
public class ProductVariationController {

    @Autowired
    private ProductVariationService productVariationService;

    // Criar uma nova variação de produto
    @PostMapping("create")
    public ResponseEntity<ProductVariationResponseDTO> create(@RequestBody ProductVariationCreateDTO productVariationCreateDTO) {
        ProductVariationResponseDTO response = productVariationService.create(productVariationCreateDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar todas as variações de produtos
    @GetMapping
    public ResponseEntity<List<ProductVariationResponseDTO>> list() {
        List<ProductVariationResponseDTO> productVariations = productVariationService.list();
        return new ResponseEntity<>(productVariations, HttpStatus.OK);
    }

    // Buscar customer por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable long id) {
        try {
            ProductVariationResponseDTO productVariation = productVariationService.show(id);
            return new ResponseEntity<>(productVariation, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Atualizar dados da variação do produto
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ProductVariationUpdateDTO productVariationUpdateDTO) {
        try {
            ProductVariationResponseDTO updated = productVariationService.update(productVariationUpdateDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Deletar um customer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable long id) {
        try {
            productVariationService.destroy(id);
            return new ResponseEntity<>("Variação de produto deletada com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }




}
