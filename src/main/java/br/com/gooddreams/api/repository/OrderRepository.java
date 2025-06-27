package br.com.gooddreams.api.repository;

import br.com.gooddreams.api.entities.Order;
import br.com.gooddreams.api.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);

}
