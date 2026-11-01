
package com.example.cartridges.repository;

import com.example.cartridges.model.Cartridge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartridgeRepository
        extends JpaRepository<Cartridge, Long> {

    Optional<Cartridge> findByModelIgnoreCase(String model);
}
