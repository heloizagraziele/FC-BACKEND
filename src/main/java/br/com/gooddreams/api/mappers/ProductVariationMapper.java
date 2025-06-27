package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.ProductVariationCreateDTO;
import br.com.gooddreams.api.dtos.ProductVariationResponseDTO;
import br.com.gooddreams.api.entities.ProductVariation;

public class ProductVariationMapper {
        public static ProductVariation toEntity(ProductVariationCreateDTO productVariationCreateDTO) {
            ProductVariation productVariation = new ProductVariation();
            productVariation.setSize(productVariationCreateDTO.size());
            productVariation.setStock(productVariationCreateDTO.stock());
            productVariation.setProduct(productVariationCreateDTO.product());
            return productVariation;
        }

    public static ProductVariationResponseDTO toDTO(ProductVariation  productVariation) {
        ProductVariationResponseDTO productVariationResponse = new ProductVariationResponseDTO(
                productVariation.getId(),
                productVariation.getSize(),
                productVariation.getStock(),
                productVariation.getProduct());
        return productVariationResponse;
    }
}
