package br.com.gooddreams.api.services;

import br.com.gooddreams.api.dtos.PromotionCreateDTO;
import br.com.gooddreams.api.dtos.PromotionResponseDTO;
import br.com.gooddreams.api.dtos.PromotionUpdateDTO;
import br.com.gooddreams.api.entities.ProductVariation;
import br.com.gooddreams.api.entities.Promotion;
import br.com.gooddreams.api.mappers.PromotionMapper;
import br.com.gooddreams.api.repository.ProductVariationRepository;
import br.com.gooddreams.api.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    public PromotionResponseDTO create(PromotionCreateDTO dto) {
        ProductVariation productVariation = productVariationRepository.findById(dto.productVariationId())
                .orElseThrow(() -> new RuntimeException("Variação de produto não encontrada."));

        Promotion promotion = PromotionMapper.toEntity(dto, productVariation);
        return PromotionMapper.toDTO(promotionRepository.save(promotion));
    }

    public List<PromotionResponseDTO> list() {
        return promotionRepository.findAll().stream()
                .map(PromotionMapper::toDTO)
                .toList();
    }

    public PromotionResponseDTO show(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoção não encontrada."));
        return PromotionMapper.toDTO(promotion);
    }

    public PromotionResponseDTO update(PromotionUpdateDTO dto) {
        Promotion promotion = promotionRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Promoção não encontrada."));
        PromotionMapper.updateEntity(promotion, dto);
        return PromotionMapper.toDTO(promotionRepository.save(promotion));
    }

    public void destroy(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoção não encontrada."));
        promotionRepository.delete(promotion);
    }
}
