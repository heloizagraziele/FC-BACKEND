package br.com.gooddreams.api.repository;

import br.com.gooddreams.api.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomerId(Long customerId);

    Optional<Address> findById(Long customerId);
}

