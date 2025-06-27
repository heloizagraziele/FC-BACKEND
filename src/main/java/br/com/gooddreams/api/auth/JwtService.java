package br.com.gooddreams.api.auth;

import br.com.gooddreams.api.entities.Customer;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value; // <--- Importar @Value
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64; // <--- Importar Base64
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;

    private final long expiration = 1000 * 60 * 60 * 24;


    public JwtService(@Value("${jwt.secret.key}") String secretKeyBase64) {

        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyBase64));
    }

    public String generateToken(Customer customer) {
        return Jwts.builder()
                .setSubject(customer.getEmail())
                .claim("name", customer.getName())
                .claim("customerId", customer.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build() // O build() ainda é necessário aqui para o parseClaimsJws
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long extractCustomerId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return ((Number) claims.get("customerId")).longValue();
    }

    public boolean isValid(String token, UserDetails user) {
        final String email = extractEmail(token);
        return (email.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expirationDate.before(new Date());
    }
}