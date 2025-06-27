package br.com.gooddreams.api.services;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.entities.ProductVariation;
import br.com.gooddreams.api.mappers.ProductVariationMapper;
import br.com.gooddreams.api.repository.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariationService {
    
    @Autowired
    private ProductVariationRepository productVariationRepository;

    public ProductVariationResponseDTO create(ProductVariationCreateDTO productVariationCreateDTO) {
        ProductVariation productVariation = ProductVariationMapper.toEntity(productVariationCreateDTO);
        ProductVariation productVariationResponse = productVariationRepository.save(productVariation);

        return ProductVariationMapper.toDTO(productVariationResponse);
    }

    public List<ProductVariationResponseDTO> list(){
        return productVariationRepository.findAll().stream().map(ProductVariationMapper::toDTO).toList();
    }

    public ProductVariationResponseDTO show(long id) {
        ProductVariation productVariation = (productVariationRepository.findById(id)).orElseThrow(()-> new RuntimeException("Cliente com o id "+id+" não foi encontrado."));

        return ProductVariationMapper.toDTO(productVariation);
    }

    public ProductVariationResponseDTO update(ProductVariationUpdateDTO productVariationUpdateDTO) {
        ProductVariation productVariation = productVariationRepository.findById(productVariationUpdateDTO.id()).orElseThrow(()-> new RuntimeException("Cliente não encontrado."));

        productVariation.setSize(productVariationUpdateDTO.size());
        productVariation.setStock(productVariationUpdateDTO.stock());
        productVariation.setProduct(productVariationUpdateDTO.product());

        return ProductVariationMapper.toDTO(productVariationRepository.save(productVariation));
    }

    public void destroy(long id) {
        ProductVariation productVariation = productVariationRepository.findById(id).orElseThrow(()-> new RuntimeException("Cliente com o id "+id+" não foi encontrado."));
        productVariationRepository.delete(productVariation);
    }
    
}
