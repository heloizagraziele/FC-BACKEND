package br.com.gooddreams.api.services;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.entities.CartItem;
import br.com.gooddreams.api.mappers.CartItemMapper;
import br.com.gooddreams.api.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItemResponseDTO create(CartItemCreateDTO cartItemCreateDTO) {
        CartItem cartItem = CartItemMapper.toEntity(cartItemCreateDTO);
        CartItem cartItemResponse = cartItemRepository.save(cartItem);

        return CartItemMapper.toDTO(cartItemResponse);
    }

    public List<CartItemResponseDTO> list(){
        return cartItemRepository.findAll().stream().map(CartItemMapper::toDTO).toList();
    }

    public List<CartItem> listarItensDoCarrinho(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    public CartItemResponseDTO show(long id) {
        CartItem cartItem = (cartItemRepository.findById(id)).orElseThrow(()-> new RuntimeException("Item do carrinho com o id "+id+" não foi encontrado."));

        return CartItemMapper.toDTO(cartItem);
    }

    public CartItemResponseDTO update(CartItemUpdateDTO cartItemUpdateDTO) {
        CartItem cartItem = cartItemRepository.findById(cartItemUpdateDTO.id()).orElseThrow(
                ()-> new RuntimeException("Item não encontrado no carrinho."));

        cartItem.setCart(cartItemUpdateDTO.cart());
        cartItem.setProductVariation(cartItemUpdateDTO.productVariation());
        cartItem.setQuantity(cartItemUpdateDTO.quantity());
        cartItem.setUnitPrice(BigDecimal.valueOf(cartItemUpdateDTO.unitPrice()));

        return CartItemMapper.toDTO(cartItemRepository.save(cartItem));
    }

    public void destroy(long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Item com o id "+id+" não foi encontrado no carrinho."));
        cartItemRepository.delete(cartItem);
    }

}
