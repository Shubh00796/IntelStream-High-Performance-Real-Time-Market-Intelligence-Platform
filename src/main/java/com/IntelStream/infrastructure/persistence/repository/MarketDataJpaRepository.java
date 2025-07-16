package com.IntelStream.infrastructure.persistence.repository;

import com.IntelStream.infrastructure.persistence.entity.MarketDataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface MarketDataJpaRepository extends JpaRepository<MarketDataEntity, Long> {

    @Query(value = """
            SELECT DISTINCT ON (instrument_id) *
            FROM market_data
            WHERE instrument_id IN ::instrumentIds
            ORDER BY instrument_id , timestamp DESC
            """, nativeQuery = true)
    List<MarketDataEntity> findLatestByInstrumentIds(@Param("instrumentIds") List<Long> instrumentIds);


    @Query(""" 
            SELECT md FROM MarketDataEntity md
            WHERE md.instrumentId = :instrumentId
            AND  md.timestamp BETWEEN :start AND :end
            ORDER BY md.timestamp DESC
            """)
    Stream<MarketDataEntity> findByInstrumentIdAndTimestampBetween(@Param("instrumentId") Long instrumentId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query(value = """
            WITH price_changes AS (
                SELECT
                    md.*,
                    LAG(md.price) OVER (PARTITION BY md.instrument_id ORDER BY md.timestamp) as prev_price,
                    ABS(md.price - LAG(md.price) OVER (PARTITION BY md.instrument_id ORDER BY md.timestamp)) /
                        LAG(md.price) OVER (PARTITION BY md.instrument_id ORDER BY md.timestamp) * 100 as price_change_percent
                FROM market_data md
                WHERE md.timestamp >= :since
            )
            SELECT * FROM price_changes
            WHERE prev_price IS NOT NULL
            AND price_change_percent IS NOT NULL
            ORDER BY price_change_percent DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<MarketDataEntity> findTopPriceMovements(@Param("limit") int limit, @Param("since") LocalDateTime since);

    @Query("""
            SELECT md FROM MarketDataEntity md 
            WHERE md.instrumentId = :instrumentId 
            AND md.timestamp = (
                SELECT MAX(md2.timestamp) FROM MarketDataEntity md2 
                WHERE md2.instrumentId = :instrumentId
            )
            """)
    Optional<MarketDataEntity> findLatestByInstrumentId(@Param("instrumentId") Long instrumentId);

    // Efficient pagination with index hints
    @Query("""
            SELECT md FROM MarketDataEntity md 
            WHERE md.instrumentId = :instrumentId 
            ORDER BY md.timestamp DESC
            """)
    Page<MarketDataEntity> findByInstrumentIdOrderByTimestampDesc(@Param("instrumentId") Long instrumentId, Pageable pageable);

    // Batch operations for high-throughput inserts
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO market_data (instrument_id, exchange_id, price, volume, bid_price, ask_price, timestamp, source, created_at, updated_at)
            VALUES (:#{#marketData.instrumentId}, :#{#marketData.exchangeId}, :#{#marketData.price}, 
                    :#{#marketData.volume}, :#{#marketData.bidPrice}, :#{#marketData.askPrice}, 
                    :#{#marketData.timestamp}, :#{#marketData.source}, NOW(), NOW())
            """, nativeQuery = true)
    void bulkInsertMarketData(@Param("marketData") MarketDataEntity marketData);

    // Data cleanup for old records
    @Modifying
    @Transactional
    @Query("DELETE FROM MarketDataEntity md WHERE md.timestamp < :cutoffDate")
    int deleteOldData(@Param("cutoffDate") LocalDateTime cutoffDate);

    // Exchange-specific queries
    List<MarketDataEntity> findByExchangeIdAndTimestampAfter(Long exchangeId, LocalDateTime timestamp);

    // Statistical queries for analytics
    @Query(value = """
            SELECT 
                AVG(price) as avg_price,
                MIN(price) as min_price,
                MAX(price) as max_price,
                STDDEV(price) as volatility,
                SUM(volume) as total_volume
            FROM market_data 
            WHERE instrument_id = :instrumentId 
            AND timestamp BETWEEN :start AND :end
            """, nativeQuery = true)
    Object[] calculateStatistics(@Param("instrumentId") Long instrumentId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
