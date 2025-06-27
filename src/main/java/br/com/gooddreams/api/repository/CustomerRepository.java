package br.com.gooddreams.api.repository;

import br.com.gooddreams.api.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.addresses a WHERE c.id = :id")
    Optional<Customer> findByIdWithAddresses(@Param("id") Long id);
}
