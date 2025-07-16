package com.IntelStream.infrastructure.persistence.repository;

import com.IntelStream.infrastructure.persistence.entity.AnalyticsSnapshotEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyticsJpaRepository extends JpaRepository<AnalyticsSnapshotEntity, Long> {

    @Query("""
            SELECT a FROM AnalyticsSnapshotEntity a
            WHERE a.instrumentId = :instrumentId
            AND a.calculatedAt = (
                SELECT MAX(a2.calculatedAt) FROM AnalyticsSnapshotEntity a2
                WHERE a2.instrumentId = :instrumentId
            )
            """)
    Optional<AnalyticsSnapshotEntity> findLatestByInstrumentId(@Param("instrumentId") Long instrumentId);

    @Query("""
            SELECT a FROM AnalyticsSnapshotEntity a
            WHERE a.instrumentId = :instrumentId
            AND a.calculatedAt BETWEEN :start AND :end
            ORDER BY a.calculatedAt DESC
            """)
    List<AnalyticsSnapshotEntity> findByInstrumentIdAndCalculatedAtBetween(
            @Param("instrumentId") Long instrumentId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Top volatile instruments using window functions
    @Query(value = """
            SELECT * FROM analytics_snapshots
            WHERE calculated_at >= :since
            ORDER BY volatility DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<AnalyticsSnapshotEntity> findTopVolatileInstruments(@Param("limit") int limit,
                                                             @Param("since") LocalDateTime since);

    // Overbought conditions (RSI > 70)
    @Query("""
            SELECT a FROM AnalyticsSnapshotEntity a
            WHERE a.rsi > 70
            AND a.calculatedAt >= :since
            ORDER BY a.rsi DESC
            """)
    List<AnalyticsSnapshotEntity> findOverboughtInstruments(@Param("since") LocalDateTime since);

    // Oversold conditions (RSI < 30)
    @Query("""
            SELECT a FROM AnalyticsSnapshotEntity a
            WHERE a.rsi < 30
            AND a.calculatedAt >= :since
            ORDER BY a.rsi ASC
            """)
    List<AnalyticsSnapshotEntity> findOversoldInstruments(@Param("since") LocalDateTime since);

    Page<AnalyticsSnapshotEntity> findByCalculatedAtBetween(
            LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Complex analytics query for market summary
    @Query(value = """
            SELECT
                COUNT(*) as total_instruments,
                AVG(volatility) as avg_volatility,
                AVG(rsi) as avg_rsi,
                SUM(volume_24h) as total_volume_24h,
                COUNT(CASE WHEN rsi > 70 THEN 1 END) as overbought_count,
                COUNT(CASE WHEN rsi < 30 THEN 1 END) as oversold_count
            FROM analytics_snapshots
            WHERE calculated_at >= :since
            """, nativeQuery = true)
    Object[] getMarketSummary(@Param("since") LocalDateTime since);
}
