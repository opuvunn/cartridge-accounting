
package com.example.cartridges.repository;

import com.example.cartridges.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository
        extends JpaRepository<Operation, Long> {
}
