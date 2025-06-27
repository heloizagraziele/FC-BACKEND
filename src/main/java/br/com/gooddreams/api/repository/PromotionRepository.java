package br.com.gooddreams.api.repository;

import br.com.gooddreams.api.entities.Product;
import br.com.gooddreams.api.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findById(Long id);
}
