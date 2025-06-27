package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.PaymentCreateDTO;
import br.com.gooddreams.api.dtos.PaymentResponseDTO;
import br.com.gooddreams.api.dtos.PaymentUpdateDTO;
import br.com.gooddreams.api.enuns.PaymentStatus;
import br.com.gooddreams.api.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("create")
    public ResponseEntity<PaymentResponseDTO> create(@RequestBody PaymentCreateDTO dto) {
        return new ResponseEntity<>(paymentService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> list() {
        return new ResponseEntity<>(paymentService.list(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(paymentService.show(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody PaymentUpdateDTO dto) {
        try {
            return new ResponseEntity<>(paymentService.update(dto), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> destroy(@PathVariable Long id) {
        try {
            paymentService.destroy(id);
            return new ResponseEntity<>("Pagamento deletado com sucesso.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approvePurchase(@PathVariable Long id) {
        PaymentResponseDTO paymentResponse = paymentService.show(id);

        if (paymentResponse != null) {
            PaymentUpdateDTO updated = new PaymentUpdateDTO(
                    paymentResponse.id(),
                    paymentResponse.order(),
                    paymentResponse.paymentMethod(),
                    PaymentStatus.PAID,
                    paymentResponse.dateTimePayment()
            );

            paymentService.update(updated);
        }

        return ResponseEntity.ok("Compra aprovada com sucesso");
    }
}
