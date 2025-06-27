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

    @Transactional // Adicione @Transactional para o método store
    public AddressResponseDTO store(AddressCreateDTO dto) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Cliente com id " + dto.customerId() + " não encontrado"));

        // --- CORREÇÃO AQUI: PASSE A ENTIDADE CUSTOMER PARA O MAPPER ---
        Address address = AddressMapper.toEntity(dto, customer); // <--- MUDANÇA AQUI
        // ---------------------------------------------------------------

        // A linha address.setCustomer(customer); abaixo deve ser removida
        // se o mapper já faz (e ele faz agora).

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

    @Transactional // <--- Adicione @Transactional para garantir a transação na atualização
    public AddressResponseDTO update(Long id_address, AddressUpdateDTO dto) { // <--- ASSINATURA CORRIGIDA: Recebe id_address
        // 1. Encontrar o endereço pelo ID vindo da URL
        Address address = addressRepository.findById(id_address) // <--- USE O ID_ADDRESS DO PARÂMETRO
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com ID: " + id_address));

        // 2. Aplicar as atualizações parciais (PATCH)
        // Apenas atualize os campos se eles vierem não nulos no DTO.
        // Se o seu frontend SEMPRE envia todos os campos (mesmo os não alterados),
        // você pode simplesmente setar todos eles. Mas a lógica abaixo é para PATCH "verdadeiro".
        if (dto.street() != null) {
            address.setStreet(dto.street());
        }
        if (dto.number() != null) { // Note: numbers can be empty strings from forms, handle as needed
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

        // REMOÇÃO IMPORTANTE: NÃO atualize o cliente (dono do endereço)
        // em um PATCH de endereço. O cliente já está associado e o endpoint
        // é para o endereço em si. Se precisar reassociar, seria outra lógica/endpoint.
        // address.setCustomer(customer); // <--- REMOVA ESTA LINHA E A BUSCA DE CLIENTE ACIMA

        // 3. Salvar as alterações
        Address updatedAddress = addressRepository.save(address);

        // 4. Retornar o DTO da resposta
        return AddressMapper.toDTO(updatedAddress);
    }

    public void destroy(long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Endereço com o id " + id + "  não encontrado."));
        addressRepository.delete(address);
    }

    public List<AddressResponseDTO> getAddressesByCustomerId(Long customerId) {
        // Busca o Customer para garantir que ele existe
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + customerId));

        // Retorna a lista de endereços associados a este cliente
        // Você pode ter um método findByCustomerId no AddressRepository para otimizar
        // List<Address> addresses = addressRepository.findByCustomerId(customerId);
        // Ou, se a entidade Customer mapeia seus endereços:
        List<Address> addresses = customer.getAddresses(); // Assumindo que Customer tem getAddresses() e @OneToMany com Address

        return addresses.stream()
                .map(AddressMapper::toDTO)
                .collect(Collectors.toList());
    }
}
