package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.CartCreateDTO;
import br.com.gooddreams.api.dtos.CartItemCreateDTO;
import br.com.gooddreams.api.dtos.CartItemResponseDTO;
import br.com.gooddreams.api.dtos.CartResponseDTO;
import br.com.gooddreams.api.entities.Cart;
import br.com.gooddreams.api.entities.CartItem;

import java.math.BigDecimal;

public class CartItemMapper {

    public static CartItem toEntity(CartItemCreateDTO cartItemCreateDTO) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cartItemCreateDTO.cart());
        cartItem.setProductVariation(cartItemCreateDTO.productVariation());
        cartItem.setQuantity(cartItemCreateDTO.quantity());
        cartItem.setUnitPrice(BigDecimal.valueOf(cartItemCreateDTO.unitPrice()));

        return cartItem;
    }

    public static CartItemResponseDTO toDTO(CartItem cartItem){
        CartItemResponseDTO cartItemResponse = new CartItemResponseDTO(
                cartItem.getId(),
                cartItem.getCart(),
                cartItem.getProductVariation(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice());

        return cartItemResponse;
    }

}
