package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.PromotionCreateDTO;
import br.com.gooddreams.api.dtos.PromotionResponseDTO;
import br.com.gooddreams.api.dtos.PromotionUpdateDTO;
import br.com.gooddreams.api.entities.ProductVariation;
import br.com.gooddreams.api.entities.Promotion;

public class PromotionMapper {

    public static Promotion toEntity(PromotionCreateDTO dto, ProductVariation productVariation) {
        Promotion promotion = new Promotion();
        promotion.setDiscountPercentage(dto.discountPercentage());
        promotion.setStartDate(dto.startDate());
        promotion.setEndDate(dto.endDate());
        promotion.setActive(dto.isActive());
        promotion.setProductVariation(productVariation);
        return promotion;
    }

    public static PromotionResponseDTO toDTO(Promotion promotion) {
        return new PromotionResponseDTO(
                promotion.getId(),
                promotion.getDiscountPercentage(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.isActive(),
                promotion.getProductVariation().getId()
        );
    }

    public static void updateEntity(Promotion promotion, PromotionUpdateDTO dto) {
        promotion.setDiscountPercentage(dto.discountPercentage());
        promotion.setStartDate(dto.startDate());
        promotion.setEndDate(dto.endDate());
        promotion.setActive(dto.isActive());
    }
}
