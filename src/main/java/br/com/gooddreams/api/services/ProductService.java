package br.com.gooddreams.api.services;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.entities.Product;
import br.com.gooddreams.api.mappers.ProductMapper;
import br.com.gooddreams.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductResponseDTO create(ProductCreateDTO productCreateDTO){
        Product product = ProductMapper.toEntity(productCreateDTO);
        Product productResponse = productRepository.save(product);

        return ProductMapper.toDTO(productResponse);
    }
    public List<ProductResponseDTO> list(){
        return productRepository.findAll().stream().map(ProductMapper::toDTO).toList();
    }

    public ProductResponseDTO show(long id) {
        Product product = productRepository.findById(id).orElseThrow(()->
                new RuntimeException("Produto com o id: "+id+" não foi encontrado."));

        return ProductMapper.toDTO(product);
    }

    public ProductResponseDTO update(ProductUpdateDTO productUpdateDTO) {
        Product product = productRepository.findById(productUpdateDTO.id())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        if (productUpdateDTO.name() != null) {
            product.setName(productUpdateDTO.name());
        }
        if (productUpdateDTO.description() != null) {
            product.setDescription(productUpdateDTO.description());
        }
        if (productUpdateDTO.price() != null){
            product.setPrice(productUpdateDTO.price());
        }
        if (productUpdateDTO.category() != null) {
            product.setCategory(productUpdateDTO.category());
        }
        if (productUpdateDTO.imageUrl() != null) {
            product.setImageUrl(productUpdateDTO.imageUrl());
        }

        Product updatedProduct = productRepository.save(product);
        return ProductMapper.toDTO(updatedProduct);
    }

    public void destroy(long id){
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Produto não encontrado.")
        );
        productRepository.delete(product);
    }

}
