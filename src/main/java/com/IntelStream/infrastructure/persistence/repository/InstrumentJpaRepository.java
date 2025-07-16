package com.IntelStream.infrastructure.persistence.repository;

import com.IntelStream.infrastructure.persistence.entity.InstrumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstrumentJpaRepository extends JpaRepository<InstrumentEntity, Long> {

    Optional<InstrumentEntity> findBySymbol(String symbol);

    @Query("SELECT i FROM InstrumentEntity i WHERE i.exchangeId = :exchangeId ORDER BY i.symbol")
    Page<InstrumentEntity> findByExchangeId(@Param("exchangeId") Long exchangeId, Pageable pageable);

    List<InstrumentEntity> findByInstrumentTypeAndActiveTrue(String instrumentType);

    @Query("SELECT i FROM InstrumentEntity i WHERE i.active = true ORDER BY i.symbol")
    List<InstrumentEntity> findActiveInstruments();

    @Query("""
        SELECT i FROM InstrumentEntity i
        WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))
        AND i.active = true
        ORDER BY i.symbol
        """)
    Page<InstrumentEntity> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    List<InstrumentEntity> findByExchangeIdAndInstrumentTypeAndActiveTrue(Long exchangeId, String instrumentType);

    @Modifying
    @Transactional
    @Query("UPDATE InstrumentEntity i SET i.active = false WHERE i.id = :id")
    int deactivateInstrument(@Param("id") Long id);

    // Advanced search with multiple criteria
    @Query("""
        SELECT i FROM InstrumentEntity i
        WHERE (:exchangeId IS NULL OR i.exchangeId = :exchangeId)
        AND (:instrumentType IS NULL OR i.instrumentType = :instrumentType)
        AND (:sector IS NULL OR i.sector = :sector)
        AND i.active = true
        ORDER BY i.symbol
        """)
    Page<InstrumentEntity> findByCriteria(@Param("exchangeId") Long exchangeId,
                                          @Param("instrumentType") String instrumentType,
                                          @Param("sector") String sector,
                                          Pageable pageable);
}