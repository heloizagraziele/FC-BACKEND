package br.com.gooddreams.api.services;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.entities.Cart;
import br.com.gooddreams.api.mappers.CartMapper;
import br.com.gooddreams.api.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public CartResponseDTO create(CartCreateDTO cartCreateDTO) {
        Cart cart = CartMapper.toEntity(cartCreateDTO);
        Cart cartResponse = cartRepository.save(cart);

        return CartMapper.toDTO(cartResponse);
    }

    public List<CartResponseDTO> list(){
        return cartRepository.findAll().stream().map(CartMapper::toDTO).toList();
    }

    public CartResponseDTO show(long id) {
        Cart cart = (cartRepository.findById(id)).orElseThrow(()-> new RuntimeException("Carrinho com o id "+id+" não foi encontrado."));

        return CartMapper.toDTO(cart);
    }

    public CartResponseDTO update(CartUpdateDTO cartUpdateDTO) {
        Cart cart = cartRepository.findById(cartUpdateDTO.id()).orElseThrow(
                ()-> new RuntimeException("Carrinho não encontrado."));

        cart.setCustomer(cartUpdateDTO.customer());

        return CartMapper.toDTO(cartRepository.save(cart));
    }

    public void destroy(long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Usuário com o id "+id+" não foi encontrado."));
        cartRepository.delete(cart);
    }
}
