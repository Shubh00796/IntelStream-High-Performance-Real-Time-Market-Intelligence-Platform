package com.IntelStream.infrastructure.persistence.repository.impl;


import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.infrastructure.persistence.entity.MarketDataEntity;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.infrastructure.persistence.repository.MarketDataJpaRepository;
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
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MarketDataRepositoryImpl implements MarketDataRepository {

    private final MarketDataJpaRepository jpaRepository;
    private final MarketDataMapper mapper;

    @Override
    @Transactional
    public MarketData save(MarketData marketData) {
        log.debug("Saving market data for instrument: {}", marketData.getInstrumentId());
        return mapToDomain(jpaRepository.save(mapper.toEntity(marketData)));
    }

    @Override
    public Optional<MarketData> findById(Long id) {
        return findAndMap(() -> jpaRepository.findById(id));
    }

    @Override
    public Page<MarketData> findByInstrumentId(Long instrumentId, Pageable pageable) {
        return jpaRepository.findByInstrumentIdOrderByTimestampDesc(instrumentId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<MarketData> findLatestByInstrumentIds(List<Long> instrumentIds) {
        log.debug("Finding latest market data for {} instruments", instrumentIds.size());
        return streamAndMap(() -> jpaRepository.findLatestByInstrumentIds(instrumentIds));
    }

    @Override
    public Stream<MarketData> findByInstrumentIdAndTimestampBetween(
            Long instrumentId, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByInstrumentIdAndTimestampBetween(instrumentId, start, end)
                .map(mapper::toDomain);
    }

    @Override
    public List<MarketData> findTopPriceMovements(int limit, LocalDateTime since) {
        return streamAndMap(() -> jpaRepository.findTopPriceMovements(limit, since));
    }

    @Override
    public Optional<MarketData> findLatestByInstrumentId(Long instrumentId) {
        return findAndMap(() -> jpaRepository.findLatestByInstrumentId(instrumentId));
    }

    @Override
    public List<MarketData> findByExchangeIdAndTimestampAfter(Long exchangeId, LocalDateTime timestamp) {
        return streamAndMap(() -> jpaRepository.findByExchangeIdAndTimestampAfter(exchangeId, timestamp));
    }

    @Override
    @Transactional
    public void deleteOldData(LocalDateTime cutoffDate) {
        log.info("Deleting market data older than: {}", cutoffDate);
        int deletedCount = jpaRepository.deleteOldData(cutoffDate);
        log.info("Deleted {} old market data records", deletedCount);
    }

    @Override
    @Transactional
    public int bulkInsert(List<MarketData> marketDataList) {
        log.debug("Bulk inserting {} market data records", marketDataList.size());
        List<MarketDataEntity> entities = marketDataList.stream()
                .map(mapper::toEntity)
                .toList();
        return jpaRepository.saveAll(entities).size();
    }

    // ========== 🔒 Private Helper Methods ==========

    private MarketData mapToDomain(MarketDataEntity entity) {
        return mapper.toDomain(entity);
    }

    private <T> Optional<MarketData> findAndMap(Supplier<Optional<T>> supplier) {
        return supplier.get().map(obj -> mapper.toDomain((MarketDataEntity) obj));
    }

    private <T> List<MarketData> streamAndMap(Supplier<List<T>> supplier) {
        return supplier.get().stream()
                .map(obj -> mapper.toDomain((MarketDataEntity) obj))
                .toList();
    }
}
