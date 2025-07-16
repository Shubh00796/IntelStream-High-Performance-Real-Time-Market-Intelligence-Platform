package com.IntelStream.infrastructure.persistence.repository;


import com.IntelStream.infrastructure.persistence.entity.ExchangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeJpaRepository extends JpaRepository<ExchangeEntity, Long> {

    // Find by natural key
    Optional<ExchangeEntity> findByCode(String code);

    // Find all active exchanges
    List<ExchangeEntity> findAllByActiveTrue();

    // Filter by currency or timezone
    List<ExchangeEntity> findByCurrency(String currency);

    List<ExchangeEntity> findByTimezone(String timezone);

    // Custom query: markets open at a given time, only active ones
    @Query("""
        SELECT e 
        FROM ExchangeEntity e 
        WHERE e.active = true
          AND e.marketOpen <= :currentTime
          AND e.marketClose >= :currentTime
    """)
    List<ExchangeEntity> findOpenMarketsAt(@Param("currentTime") LocalDateTime currentTime);

    // Soft-deactivate an exchange
    @Modifying
    @Transactional
    @Query("UPDATE ExchangeEntity e SET e.active = false WHERE e.id = :id")
    void deactivateExchange(@Param("id") Long id);
}
