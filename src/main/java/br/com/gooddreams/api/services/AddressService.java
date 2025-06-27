package br.com.gooddreams.api.services;


import br.com.gooddreams.api.dtos.AddressCreateDTO;
import br.com.gooddreams.api.dtos.AddressResponseDTO;
import br.com.gooddreams.api.dtos.AddressUpdateDTO;
import br.com.gooddreams.api.entities.Address;
import br.com.gooddreams.api.entities.Customer;
import br.com.gooddreams.api.mappers.AddressMapper;
import br.com.gooddreams.api.repository.AddressRepository;
import br.com.gooddreams.api.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    @Transactional
    public AddressResponseDTO store(AddressCreateDTO dto) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Cliente com id " + dto.customerId() + " não encontrado"));


        Address address = AddressMapper.toEntity(dto, customer);

        Address saved = addressRepository.save(address);
        return AddressMapper.toDTO(saved);
    }


    public List<AddressResponseDTO> list() {
        return addressRepository.findAll().stream().map(AddressMapper::toDTO).toList();
    }

    public AddressResponseDTO show(long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Endereço com id" + id + " não encontrado"));
        return AddressMapper.toDTO(address);
    }

    @Transactional
    public AddressResponseDTO update(Long id_address, AddressUpdateDTO dto) {

        Address address = addressRepository.findById(id_address)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com ID: " + id_address));

        if (dto.street() != null) {
            address.setStreet(dto.street());
        }
        if (dto.number() != null) {
            address.setNumber(dto.number());
        }
        if (dto.neighborhood() != null) {
            address.setNeighborhood(dto.neighborhood());
        }
        if (dto.city() != null) {
            address.setCity(dto.city());
        }
        if (dto.state() != null) {
            address.setState(dto.state());
        }
        if (dto.complement() != null) {
            address.setComplement(dto.complement());
        }
        if (dto.zipcode() != null) {
            address.setZipcode(dto.zipcode());
        }
        Address updatedAddress = addressRepository.save(address);
        return AddressMapper.toDTO(updatedAddress);
    }

    public void destroy(long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Endereço com o id " + id + "  não encontrado."));
        addressRepository.delete(address);
    }

    public List<AddressResponseDTO> getAddressesByCustomerId(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + customerId));

        List<Address> addresses = customer.getAddresses();

        return addresses.stream()
                .map(AddressMapper::toDTO)
                .collect(Collectors.toList());
    }
}
