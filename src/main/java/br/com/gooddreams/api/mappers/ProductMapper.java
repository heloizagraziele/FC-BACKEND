package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.ProductCreateDTO;
import br.com.gooddreams.api.dtos.ProductResponseDTO;
import br.com.gooddreams.api.entities.Product;

public class ProductMapper {

    public static Product toEntity(ProductCreateDTO productCreateDTO) {
        Product product = new Product();
        product.setName(productCreateDTO.name());
        product.setDescription(productCreateDTO.description());
        product.setPrice(productCreateDTO.price());
        product.setCategory(productCreateDTO.category());
        product.setImageUrl(productCreateDTO.imageUrl());

        return product;
    }

    public static ProductResponseDTO toDTO(Product product) {
        ProductResponseDTO productResponse = new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getImageUrl(),
                product.getCreatedAt(),
                product.getUpdatedAt());

        return productResponse;
    }
}
