package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.CartCreateDTO;
import br.com.gooddreams.api.dtos.CartResponseDTO;
import br.com.gooddreams.api.entities.Cart;

public class CartMapper {

    public static Cart toEntity(CartCreateDTO cartCreateDTO){
        Cart cart = new Cart();
        cart.setCustomer(cartCreateDTO.customer());

        return cart;
    }

public static CartResponseDTO toDTO(Cart cart){
        CartResponseDTO cartResponse = new CartResponseDTO(
        cart.getId(),
        cart.getCustomer());
        return cartResponse;
}
}
