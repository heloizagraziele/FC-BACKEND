package br.com.gooddreams.api.repository;

import br.com.gooddreams.api.entities.Product;
import br.com.gooddreams.api.entities.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariationRepository extends JpaRepository <ProductVariation, Long> {
    Optional<ProductVariation> findById(Long id);
}
