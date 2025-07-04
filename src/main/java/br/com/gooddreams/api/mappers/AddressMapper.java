package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.AddressCreateDTO;
import br.com.gooddreams.api.dtos.AddressResponseDTO;
import br.com.gooddreams.api.dtos.AddressUpdateDTO;
import br.com.gooddreams.api.entities.Address;
import br.com.gooddreams.api.entities.Customer;
import java.util.Optional;

public class AddressMapper {

    public static AddressResponseDTO toDTO(Address address) {
        if (address == null) {
            return null;
        }

        AddressResponseDTO addressResponseDTO = new AddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),

                address.getZipcode() != null ? Long.parseLong(address.getZipcode()) : null,


                address.getCustomer() != null ? address.getCustomer().getId() : null

        );
        return addressResponseDTO;
    }


    public static Address toEntity(AddressCreateDTO dto, Customer customer) {
        if (dto == null || customer == null) {
            throw new IllegalArgumentException("AddressCreateDTO e Customer não podem ser nulos.");
        }
        Address address = new Address();
        address.setCustomer(customer);
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setNeighborhood(dto.neighborhood());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setComplement(dto.complement());

        address.setZipcode(String.valueOf(dto.zipcode()));


        return address;
    }


    public static void updateEntity(Address address, AddressUpdateDTO dto) {
        if (address == null || dto == null) {
            return;
        }

        Optional.ofNullable(dto.street()).ifPresent(address::setStreet);
        Optional.ofNullable(dto.number()).ifPresent(address::setNumber);
        Optional.ofNullable(dto.complement()).ifPresent(address::setComplement);
        Optional.ofNullable(dto.neighborhood()).ifPresent(address::setNeighborhood);
        Optional.ofNullable(dto.city()).ifPresent(address::setCity);
        Optional.ofNullable(dto.state()).ifPresent(address::setState);

        Optional.ofNullable(dto.zipcode()).ifPresent(zip -> address.setZipcode(String.valueOf(zip)));

    }


}