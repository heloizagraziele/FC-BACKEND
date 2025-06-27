package br.com.gooddreams.api.repository;

import br.com.gooddreams.api.entities.Cart;
import br.com.gooddreams.api.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCustomerId(Long customerId);
}
