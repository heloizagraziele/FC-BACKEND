package br.com.gooddreams.api.repository;

import br.com.gooddreams.api.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);
    List<Order> findByCustomerId(Long customerId);

}
