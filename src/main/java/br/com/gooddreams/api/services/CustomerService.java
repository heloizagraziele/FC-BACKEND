package br.com.gooddreams.api.services;

import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.entities.Address; // Importar a entidade Address
import br.com.gooddreams.api.entities.Customer;
import br.com.gooddreams.api.mappers.AddressMapper; // <--- IMPORTAR ADDRESSMAPPER
import br.com.gooddreams.api.mappers.CustomerMapper;
import br.com.gooddreams.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerResponseDTO store(CustomerCreateDTO customerCreateDTO) {
        Customer customer = CustomerMapper.toEntity(customerCreateDTO);
        customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.toDTO(savedCustomer);
    }

    public List<CustomerResponseDTO> list() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerProfileDTO show(long id_customer) {
        Customer customer = customerRepository.findByIdWithAddresses(id_customer)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // --- CORREÇÃO AQUI: REUTILIZAR O AddressMapper.toDTO ---
        List<AddressResponseDTO> addressDTOs = customer.getAddresses().stream()
                .map(AddressMapper::toDTO) // <--- Use o método toDTO do AddressMapper
                .collect(Collectors.toList());
        // -----------------------------------------------------

        return new CustomerProfileDTO(
                customer.getName(),
                customer.getEmail(),
                addressDTOs
        );
    }

    @Transactional
    public CustomerProfileDTO updateCustomer(Long id_customer, CustomerUpdateDTO dto) {
        Customer customer = customerRepository.findById(id_customer)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id_customer));

        if (dto.name() != null && !dto.name().trim().isEmpty()) {
            customer.setName(dto.name().trim());
        }

        if (dto.email() != null && !dto.email().trim().isEmpty()) {
            Optional<Customer> existingCustomerWithEmail = customerRepository.findByEmail(dto.email().trim());
            if (existingCustomerWithEmail.isPresent() && !existingCustomerWithEmail.get().getId().equals(customer.getId())) {
                throw new RuntimeException("Este email já está em uso por outro cliente.");
            }
            customer.setEmail(dto.email().trim());
        }

        if (dto.password() != null && !dto.password().isEmpty()) {
            customer.setPassword(passwordEncoder.encode(dto.password()));
        }

        Customer updatedCustomer = customerRepository.save(customer);

        return show(updatedCustomer.getId());
    }

    public void destroy(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente com o id " + id + " não foi encontrado."));
        customerRepository.delete(customer);
    }

}