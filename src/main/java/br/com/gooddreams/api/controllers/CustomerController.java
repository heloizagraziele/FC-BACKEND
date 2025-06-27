package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.auth.JwtService;
import br.com.gooddreams.api.dtos.*;
import br.com.gooddreams.api.entities.Customer;
import br.com.gooddreams.api.repository.CustomerRepository;
import br.com.gooddreams.api.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public CustomerController(CustomerService customerService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // --- CORREÇÃO: RETORNAR AUTHRESPONSEDTO COMPLETO COM EMAIL ---
    @PostMapping("/auth")
    public ResponseEntity<AuthResponseDTO> auth(@RequestBody AuthRequestDTO authDTO) {
        System.out.println("LOGIN TENTADO: " + authDTO.email());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.email(), authDTO.password()));

        Customer customer = (Customer) authentication.getPrincipal(); // Assuming getPrincipal() returns Customer entity directly
        String token = jwtService.generateToken(customer);

        // Retorna o DTO completo com email, name e customerId
        return ResponseEntity.ok(new AuthResponseDTO(token, customer.getName(), customer.getEmail(), customer.getId()));
    }
    // -----------------------------------------------------------------

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> store(@RequestBody CustomerCreateDTO customerCreateDTO) {
        CustomerResponseDTO createdCustomer = customerService.store(customerCreateDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> list() {
        return new ResponseEntity<>(customerService.list(), HttpStatus.OK);
    }

    @GetMapping("/{id_customer}")
    public ResponseEntity<?> show(@PathVariable("id_customer") Long id_customer) {
        try {
            CustomerProfileDTO profile = customerService.show(id_customer);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            System.err.println("Erro ao buscar usuário com ID " + id_customer + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao buscar usuário.");
        }
    }

    // --- REMOVIDO: Método getAddresses será movido para AddressController ---
    // @GetMapping("/customers/addresses/{id}")
    // public ResponseEntity<List<AddressResponseDTO>> getAddresses(@PathVariable Long id) {
    //     Customer customer = customerRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    //
    //     List<Address> addresses = customer.getAddresses();
    //     List<AddressResponseDTO> dtos = addresses.stream()
    //             .map(AddressMapper::toDTO)
    //             .toList();
    //
    //     return ResponseEntity.ok(dtos);
    // }
    // ----------------------------------------------------------------------

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody CustomerUpdateDTO customerUpdateDTO) {
        try {
            return ResponseEntity.ok(customerService.updateCustomer(customerUpdateDTO.id(), customerUpdateDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível atualizar o usuário.");
        }
    }

    // Você tem dois métodos @PatchMapping sem rota específica ou com rotas duplicadas.
    // O segundo @PatchMapping("/{id_customer}") parece mais completo. Mantenha um ou ajuste as rotas.
    // Exemplo: @PatchMapping("/{id_customer}") para atualização por ID específica.
    // Se o de cima for para atualização do próprio usuário autenticado, use /me.
    @PatchMapping("/{id_customer}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id_customer") Long id_customer,
                                            @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        try {
            CustomerProfileDTO updatedProfile = customerService.updateCustomer(id_customer, customerUpdateDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            System.err.println("Erro ao atualizar cliente com ID " + id_customer + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao atualizar cliente.");
        }
    }
}