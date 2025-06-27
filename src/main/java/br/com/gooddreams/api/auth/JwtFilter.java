package br.com.gooddreams.api.auth;

import br.com.gooddreams.api.entities.Customer; // Importe Customer
import br.com.gooddreams.api.repository.CustomerRepository; // Importe CustomerRepository
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired; // Adicionado
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    private CustomerRepository customerRepository;

    public JwtFilter(JwtService jwtService, CustomerRepository customerRepository) {
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // --- PERMITIR ACESSO PÚBLICO A PRODUTOS E AUTENTICAÇÃO ---
        if (path.startsWith("/api/auth") ||
                path.startsWith("/api/customer/register") ||
                path.equals("/api/products") ||
                path.startsWith("/api/products/") ||
                path.startsWith("/api/home")
        ) {
            System.out.println("JWT Filter: Path '" + path + "' bypassed (Public Access).");
            filterChain.doFilter(request, response);
            return;
        }
        // -----------------------------------------------------------------------------

        String authorization = request.getHeader("Authorization");


        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("JWT Filter: No valid Authorization header found for protected path '" + path + "'. Access will be denied by Spring Security.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);
        String email = null;
        String requestId = request.getHeader("X-Request-ID"); // Pega o requestId se estiver no header
        if (requestId == null || requestId.isEmpty()) requestId = "N/A-" + System.currentTimeMillis();

        System.out.println(String.format("[Backend - %s] JWT Filter: Processing protected path '%s'. Token: %s...", requestId, path, token.substring(0, Math.min(token.length(), 30)))); // Loga parte do token

        try {
            email = jwtService.extractEmail(token);
            System.out.println(String.format("[Backend - %s] JWT Filter: Extracted email: %s.", requestId, email));
        } catch (Exception e) {
            System.err.println(String.format("[Backend - %s] JWT Filter: Error extracting email from token for path '%s': %s", requestId, path, e.getMessage()));

            filterChain.doFilter(request, response);
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = null;
            try {
                Customer customer = customerRepository.findByEmail(email)
                        .orElse(null);

                if (customer != null) {
                    user = customer; // Assumindo que sua entidade Customer implementa UserDetails
                } else {
                    System.out.println(String.format("[Backend - %s] JWT Filter: Customer not found for email: %s.", requestId, email));
                }

                System.out.println(String.format("[Backend - %s] JWT Filter: User loaded for email '%s': %s.", requestId, email, (user != null ? user.getUsername() : "null")));
            } catch (Exception e) {
                System.err.println(String.format("[Backend - %s] JWT Filter: Error loading user details for email '%s': %s", requestId, email, e.getMessage()));
            }

            if (user != null && jwtService.isValid(token, user)) {
                System.out.println(String.format("[Backend - %s] JWT Filter: Token is valid for user '%s'. Setting authentication.", requestId, email));
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println(String.format("[Backend - %s] JWT Filter: Token is NOT valid or user is null for email '%s'. Authentication NOT set.", requestId, email));
            }
        } else if (SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println(String.format("[Backend - %s] JWT Filter: Authentication already exists in SecurityContext for path '%s'. Skipping.", requestId, path));
        } else if (email == null) {
            System.out.println(String.format("[Backend - %s] JWT Filter: Email could not be extracted from token for path '%s'. Authentication NOT set.", requestId, path));
        }

        filterChain.doFilter(request, response);
    }
}