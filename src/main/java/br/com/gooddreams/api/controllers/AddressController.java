package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.AddressCreateDTO;
import br.com.gooddreams.api.dtos.AddressResponseDTO;
import br.com.gooddreams.api.dtos.AddressUpdateDTO;
import br.com.gooddreams.api.entities.Customer; // Importe Customer
import br.com.gooddreams.api.mappers.AddressMapper; // Importe AddressMapper
import br.com.gooddreams.api.services.AddressService;
import br.com.gooddreams.api.repository.CustomerRepository; // Certifique-se de injetar se precisar buscar o Customer

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // Para obter o usuário autenticado
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Você pode precisar injetar o CustomerRepository ou CustomerService se AddressService não retornar o Customer completo
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> store(@RequestBody AddressCreateDTO addressCreateDTO, Principal principal) {
        // Validação de segurança: o customerId do DTO deve ser do usuário autenticado
        // Isso assume que principal.getName() retorna o email do usuário
        Customer authenticatedCustomer = customerRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

        if (!authenticatedCustomer.getId().equals(addressCreateDTO.customerId())) {
            // Se o customerId no payload não for do usuário autenticado, nega o acesso
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Retorna 403 Forbidden
        }

        // Se o customerId do DTO for apenas um placeholder, você pode até ignorá-lo no service e usar o authenticatedCustomerId
        // addressCreateDTO.setCustomerId(authenticatedCustomer.getId()); // Se AddressCreateDTO for uma classe mutável

        return new ResponseEntity<>(addressService.store(addressCreateDTO), HttpStatus.CREATED);
    }

    // Este endpoint de GET sem parâmetros deve ser avaliado.
    // Se ele lista TODOS os endereços da base, deve ser restrito a ADMIN.
    // Se ele deve listar os endereços do usuário autenticado, use a rota abaixo (getAddressesForAuthenticatedCustomer).
    @GetMapping // CUIDADO: Este endpoint pode expor todos os endereços se não houver filtro de segurança global.
    public ResponseEntity<List<AddressResponseDTO>> list()
    {
        // Se este método deve listar endereços para o admin, ele precisaria de @PreAuthorize("hasRole('ADMIN')")
        // Ou se ele deve listar os próprios endereços do usuário autenticado:
        // throw new RuntimeException("Este endpoint deve ser /api/addresses/me ou /api/addresses/customer/{customerId} e seguro.");
        return new ResponseEntity<>(addressService.list(), HttpStatus.OK); // Assume que addressService.list() filtra por user ou é para admin
    }

    // --- NOVO/CORRIGIDO: Endpoint para buscar endereços do cliente autenticado ---
    @GetMapping("/me") // Ex: GET /api/addresses/me
    public ResponseEntity<List<AddressResponseDTO>> getAddressesForAuthenticatedCustomer(Principal principal) {
        String authenticatedUserEmail = principal.getName(); // Email do usuário autenticado

        // Busca o cliente autenticado pelo email
        Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

        // Busca os endereços associados a este cliente
        // Você precisará de um método em AddressService como findAddressesByCustomerId
        List<AddressResponseDTO> addresses = addressService.getAddressesByCustomerId(authenticatedCustomer.getId());
        return ResponseEntity.ok(addresses);
    }

    // --- NOVO/CORRIGIDO: Endpoint para buscar endereços por ID do Cliente (APENAS SE NECESSÁRIO E MAIS SEGURO) ---
    // Este endpoint é menos seguro que /me, pois expõe o ID do cliente na URL.
    // Se mantido, DEVE ter validação forte se o usuário autenticado pode ver os endereços de 'customerId'.
    // Ex: @PreAuthorize("#customerId == authentication.principal.id") - se principal for a entidade Customer
    @GetMapping("/customer/{customerId}") // Ex: GET /api/addresses/customer/11
    public ResponseEntity<List<AddressResponseDTO>> getAddressesByCustomerId(@PathVariable Long customerId, Principal principal) {
        String authenticatedUserEmail = principal.getName();
        Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

        // VALIDAÇÃO CRÍTICA: Permite buscar endereços APENAS se o customerId na URL for o do usuário autenticado
        if (!authenticatedCustomer.getId().equals(customerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Acesso negado
        }

        List<AddressResponseDTO> addresses = addressService.getAddressesByCustomerId(customerId);
        return ResponseEntity.ok(addresses);
    }
    // -------------------------------------------------------------------------------------------------------

    @GetMapping("/{id_address}")
    public ResponseEntity<AddressResponseDTO> show(@PathVariable long id_address, Principal principal){
        try {
            // Segurança: Garanta que o endereço pertence ao usuário autenticado
            String authenticatedUserEmail = principal.getName();
            Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedUserEmail)
                    .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

            AddressResponseDTO address = addressService.show(id_address);
            if (!address.customerId().equals(authenticatedCustomer.getId())) { // Assumindo AddressResponseDTO tem customerId()
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity(address,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id_address}")
    public ResponseEntity<AddressResponseDTO> update(
            @PathVariable("id_address") Long id_address,
            @RequestBody AddressUpdateDTO addressUpdateDTO,
            Principal principal) {
        try {
            // Segurança: Garanta que o endereço pertence ao usuário autenticado
            String authenticatedUserEmail = principal.getName();
            Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedUserEmail)
                    .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

            AddressResponseDTO existingAddress = addressService.show(id_address);
            if (!existingAddress.customerId().equals(authenticatedCustomer.getId())) {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity(addressService.update(id_address, addressUpdateDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id_address}")
    public ResponseEntity<AddressResponseDTO> destroy(@PathVariable long id_address, Principal principal){
        try {
            // Segurança: Garanta que o endereço pertence ao usuário autenticado
            String authenticatedUserEmail = principal.getName();
            Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedUserEmail)
                    .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

            AddressResponseDTO existingAddress = addressService.show(id_address);
            if (!existingAddress.customerId().equals(authenticatedCustomer.getId())) {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            addressService.destroy(id_address);
            return new ResponseEntity("Endereço deletado com sucesso",HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}