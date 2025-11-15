package com.prgx.migration.api.util.repository;

import com.prgx.migration.api.util.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Guest entity
 */
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByUserId(Long userId);

    Optional<Guest> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    long countByUserId(Long userId);
}

