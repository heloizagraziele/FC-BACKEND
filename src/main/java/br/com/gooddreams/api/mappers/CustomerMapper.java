package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.CustomerCreateDTO;
import br.com.gooddreams.api.dtos.CustomerResponseDTO;
import br.com.gooddreams.api.entities.Customer;

public class CustomerMapper {

    public static Customer toEntity(CustomerCreateDTO customerCreateDTO) {
        Customer customer = new Customer();
        customer.setName(customerCreateDTO.name());
        customer.setEmail(customerCreateDTO.email());
        customer.setPassword(customerCreateDTO.password());
        return customer;
    }

    public static CustomerResponseDTO toDTO(Customer customer) {
        CustomerResponseDTO customerResponse = new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getCreatedAt(),
                customer.getUpdateAt());
        return customerResponse;
    }
}
