package br.com.gooddreams.api.repository;

import br.com.gooddreams.api.entities.Order;
import br.com.gooddreams.api.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findById(Long id);

    Optional<Payment> findByOrder(Order order);
}
