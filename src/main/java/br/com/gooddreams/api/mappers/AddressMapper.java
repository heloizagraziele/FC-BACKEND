package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.AddressCreateDTO;
import br.com.gooddreams.api.dtos.AddressResponseDTO;
import br.com.gooddreams.api.dtos.AddressUpdateDTO;
import br.com.gooddreams.api.entities.Address;
import br.com.gooddreams.api.entities.Customer; // <--- IMPORTE A ENTIDADE CUSTOMER
import java.util.Optional;

public class AddressMapper {

    public static AddressResponseDTO toDTO(Address address) {
        if (address == null) {
            return null; // Sempre bom verificar nulo
        }

        AddressResponseDTO addressResponseDTO = new AddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                // --- CORREÇÃO AQUI: Converter String para Long para o DTO ---
                address.getZipcode() != null ? Long.parseLong(address.getZipcode()) : null, // Assumindo zipcode na entidade é String
                // -------------------------------------------------------------
                // --- CORREÇÃO AQUI: Obter o ID do Customer ---
                address.getCustomer() != null ? address.getCustomer().getId() : null
                // -------------------------------------------
        );
        return addressResponseDTO;
    }

    // --- CORREÇÃO AQUI: Método toEntity agora recebe a entidade Customer como PARÂMETRO ---
    public static Address toEntity(AddressCreateDTO dto, Customer customer) { // <--- Adicionado 'Customer customer'
        if (dto == null || customer == null) { // 'customer' agora está definido aqui
            throw new IllegalArgumentException("AddressCreateDTO e Customer não podem ser nulos.");
        }
        Address address = new Address();

        // --- CORREÇÃO AQUI: Associar o Customer passado como parâmetro ---
        address.setCustomer(customer);
        // ---------------------------------------------------------------

        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setNeighborhood(dto.neighborhood());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setComplement(dto.complement());
        // --- CORREÇÃO AQUI: Converter Long do DTO para String da entidade ---
        address.setZipcode(String.valueOf(dto.zipcode()));
        // -----------------------------------------------------------------

        return address;
    }

    // --- MÉTODO updateEntity (VERIFICAR SE ESTÁ ATUALIZADO NO SEU CÓDIGO) ---
    public static void updateEntity(Address address, AddressUpdateDTO dto) {
        if (address == null || dto == null) {
            return; // Ou lance exceção
        }

        Optional.ofNullable(dto.street()).ifPresent(address::setStreet);
        Optional.ofNullable(dto.number()).ifPresent(address::setNumber);
        Optional.ofNullable(dto.complement()).ifPresent(address::setComplement);
        Optional.ofNullable(dto.neighborhood()).ifPresent(address::setNeighborhood);
        Optional.ofNullable(dto.city()).ifPresent(address::setCity);
        Optional.ofNullable(dto.state()).ifPresent(address::setState);

        // --- CORREÇÃO AQUI: Converter Long do DTO para String da entidade no update ---
        Optional.ofNullable(dto.zipcode()).ifPresent(zip -> address.setZipcode(String.valueOf(zip)));
        // --------------------------------------------------------------------------
    }

    // Removido o método generateDefaultAlias, pois o alias não é mais usado
}