package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.PromotionCreateDTO;
import br.com.gooddreams.api.dtos.PromotionResponseDTO;
import br.com.gooddreams.api.dtos.PromotionUpdateDTO;
import br.com.gooddreams.api.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping("create")
    public ResponseEntity<PromotionResponseDTO> create(@RequestBody PromotionCreateDTO dto) {
        return new ResponseEntity<>(promotionService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PromotionResponseDTO>> list() {
        return new ResponseEntity<>(promotionService.list(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(promotionService.show(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody PromotionUpdateDTO dto) {
        try {
            return new ResponseEntity<>(promotionService.update(dto), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable Long id) {
        try {
            promotionService.destroy(id);
            return new ResponseEntity<>("Promoção deletada com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

