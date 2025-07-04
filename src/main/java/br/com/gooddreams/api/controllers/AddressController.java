package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.AddressCreateDTO;
import br.com.gooddreams.api.dtos.AddressResponseDTO;
import br.com.gooddreams.api.dtos.AddressUpdateDTO;
import br.com.gooddreams.api.entities.Customer;
import br.com.gooddreams.api.services.AddressService;
import br.com.gooddreams.api.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> store(@RequestBody AddressCreateDTO addressCreateDTO, Principal principal) {

        Customer authenticatedCustomer = customerRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

        if (!authenticatedCustomer.getId().equals(addressCreateDTO.customerId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }



        return new ResponseEntity<>(addressService.store(addressCreateDTO), HttpStatus.CREATED);
    }


    public ResponseEntity<List<AddressResponseDTO>> list()
    {
        return new ResponseEntity<>(addressService.list(), HttpStatus.OK);
    }


    @GetMapping("/me")
    public ResponseEntity<List<AddressResponseDTO>> getAddressesForAuthenticatedCustomer(Principal principal) {
        String authenticatedUserEmail = principal.getName();


        Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

        List<AddressResponseDTO> addresses = addressService.getAddressesByCustomerId(authenticatedCustomer.getId());
        return ResponseEntity.ok(addresses);
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AddressResponseDTO>> getAddressesByCustomerId(@PathVariable Long customerId, Principal principal) {
        String authenticatedUserEmail = principal.getName();
        Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

        if (!authenticatedCustomer.getId().equals(customerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<AddressResponseDTO> addresses = addressService.getAddressesByCustomerId(customerId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id_address}")
    public ResponseEntity<AddressResponseDTO> show(@PathVariable long id_address, Principal principal){
        try {

            String authenticatedUserEmail = principal.getName();
            Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedUserEmail)
                    .orElseThrow(() -> new RuntimeException("Cliente autenticado não encontrado."));

            AddressResponseDTO address = addressService.show(id_address);
            if (!address.customerId().equals(authenticatedCustomer.getId())) {
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