package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.auth.JwtService;
import br.com.gooddreams.api.dtos.AuthRequestDTO;
import br.com.gooddreams.api.dtos.AuthResponseDTO;
import br.com.gooddreams.api.entities.Customer; // Importe a entidade Customer
import br.com.gooddreams.api.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          CustomerRepository customerRepository,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO dto) {
        System.out.println("LOGIN TENTADO: " + dto.email());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        Customer customer = customerRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(customer);

        // <--- AQUI ESTÁ A MUDANÇA PRINCIPAL: Incluir o customer.getEmail()
        return ResponseEntity.ok(new AuthResponseDTO(token, customer.getName(), customer.getEmail(), customer.getId()));
    }
}