package com.IntelStream.infrastructure.persistence.repository.impl;


import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.domain.repository.AnalyticsRepository;
import com.IntelStream.infrastructure.persistence.entity.AnalyticsSnapshotEntity;
import com.IntelStream.infrastructure.persistence.mapper.AnalyticsSnapshotMapper;
import com.IntelStream.infrastructure.persistence.repository.AnalyticsJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AnalyticsRepositoryImpl implements AnalyticsRepository {

    private final AnalyticsJpaRepository jpaRepository;
    private final AnalyticsSnapshotMapper mapper;

    @Override
    @Transactional
    public AnalyticsSnapshot save(AnalyticsSnapshot analyticsSnapshot) {
        log.debug("Saving analytics snapshot for instrumentId: {}", analyticsSnapshot.getInstrumentId());
        AnalyticsSnapshotEntity entity = mapper.toEntity(analyticsSnapshot);
        AnalyticsSnapshotEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<AnalyticsSnapshot> findById(Long id) {
        return findAndMap(() -> jpaRepository.findById(id));
    }

    @Override
    public Optional<AnalyticsSnapshot> findLatestByInstrumentId(Long instrumentId) {
        return findAndMap(() -> jpaRepository.findLatestByInstrumentId(instrumentId));
    }

    @Override
    public List<AnalyticsSnapshot> findByInstrumentIdAndCalculatedAtBetween(
            Long instrumentId, LocalDateTime start, LocalDateTime end) {
        return streamAndMap(() -> jpaRepository.findByInstrumentIdAndCalculatedAtBetween(instrumentId, start, end));
    }

    @Override
    public List<AnalyticsSnapshot> findTopVolatileInstruments(int limit, LocalDateTime since) {
        return streamAndMap(() -> jpaRepository.findTopVolatileInstruments(limit, since));
    }

    @Override
    public List<AnalyticsSnapshot> findOverboughtInstruments(LocalDateTime since) {
        return streamAndMap(() -> jpaRepository.findOverboughtInstruments(since));
    }

    @Override
    public List<AnalyticsSnapshot> findOversoldInstruments(LocalDateTime since) {
        return streamAndMap(() -> jpaRepository.findOversoldInstruments(since));
    }

    @Override
    public Page<AnalyticsSnapshot> findByCalculatedAtBetween(
            LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return jpaRepository.findByCalculatedAtBetween(start, end, pageable)
                .map(mapper::toDomain);
    }

    // ======= 🔒 PRIVATE MAPPING HELPERS =======

    private <T> Optional<AnalyticsSnapshot> findAndMap(Supplier<Optional<T>> supplier) {
        return supplier.get().map(obj -> mapper.toDomain((AnalyticsSnapshotEntity) obj));
    }

    private <T> List<AnalyticsSnapshot> streamAndMap(Supplier<List<T>> supplier) {
        return supplier.get().stream()
                .map(obj -> mapper.toDomain((AnalyticsSnapshotEntity) obj))
                .collect(Collectors.toList());
    }
}
