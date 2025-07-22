package com.IntelStream.domain.repository;


import com.IntelStream.domain.model.MarketData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface MarketDataRepository {

    MarketData save(MarketData marketData);

    Optional<MarketData> findById(Long id);

    Page<MarketData> findByInstrumentId(Long instrumentId, Pageable pageable);

    List<MarketData> findLatestByInstrumentIds(List<Long> instrumentIds);

    Stream<MarketData> findByInstrumentIdAndTimestampBetween(
            Long instrumentId, LocalDateTime start, LocalDateTime end);

    List<MarketData> findTopPriceMovements(int limit, LocalDateTime since);

    Optional<MarketData> findLatestByInstrumentId(Long instrumentId);

    List<MarketData> findByExchangeIdAndTimestampAfter(Long exchangeId, LocalDateTime timestamp);

    void deleteOldData(LocalDateTime cutoffDate);

    int bulkInsert(List<MarketData> marketDataList);


    List<MarketData> findRecentData(Long instrumentId);

}