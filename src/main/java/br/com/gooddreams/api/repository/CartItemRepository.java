package br.com.gooddreams.api.repository;

import br.com.gooddreams.api.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findById(Long id);
    List<CartItem> findByCartId(Long cartId);

}
