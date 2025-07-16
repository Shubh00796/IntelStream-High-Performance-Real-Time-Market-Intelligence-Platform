package com.IntelStream.domain.repository;

import com.IntelStream.domain.model.AnalyticsSnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnalyticsRepository {

    AnalyticsSnapshot save(AnalyticsSnapshot analyticsSnapshot);

    Optional<AnalyticsSnapshot> findById(Long id);

    Optional<AnalyticsSnapshot> findLatestByInstrumentId(Long instrumentId);

    List<AnalyticsSnapshot> findByInstrumentIdAndCalculatedAtBetween(
            Long instrumentId, LocalDateTime start, LocalDateTime end);

    List<AnalyticsSnapshot> findTopVolatileInstruments(int limit, LocalDateTime since);

    List<AnalyticsSnapshot> findOverboughtInstruments(LocalDateTime since);

    List<AnalyticsSnapshot> findOversoldInstruments(LocalDateTime since);

    Page<AnalyticsSnapshot> findByCalculatedAtBetween(
            LocalDateTime start, LocalDateTime end, Pageable pageable);
}