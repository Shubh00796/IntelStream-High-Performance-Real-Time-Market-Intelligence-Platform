package com.IntelStream.application.query.service.analytics;

import com.IntelStream.application.query.dto.AnalyticsQuery;
import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.repository.AnalyticsRepository;
import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.domain.service.analytics.MarketAnalyticsService;
import com.IntelStream.infrastructure.persistence.mapper.AnalyticsSnapshotMapper;
import com.IntelStream.presentation.dto.response.AnalyticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsQueryService {

    private final AnalyticsRepository repo;
    private final AnalyticsSnapshotMapper mapper;
    private final MarketDataRepository marketDataRepository;
    private final MarketAnalyticsService marketAnalyticsService;

    @Cacheable(cacheNames = "analytics", key = "'id:' + #id")
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public AnalyticsResponse getById(Long id) {
        return mapOrThrow(repo.findById(id), "AnalyticsSnapshot not found with ID: " + id);
    }

    @Cacheable(cacheNames = "analytics", key = "'latest:' + #instrumentId")
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public AnalyticsResponse getLatestByInstrument(Long instrumentId) {
        return mapOrThrow(repo.findLatestByInstrumentId(instrumentId), "No analytics available for instrument: " + instrumentId);
    }

    public List<AnalyticsResponse> search(AnalyticsQuery query) {
        List<AnalyticsSnapshot> raw = repo.findByInstrumentIdAndCalculatedAtBetween(query.getInstrumentId(), query.getStartTime(), query.getEndTime());

        return raw.stream().filter(applyFilters(query)).map(mapper::toResponce).toList();
    }

    public List<AnalyticsResponse> getTopVolatile(int limit, LocalDateTime since) {
        return mapAll(repo.findTopVolatileInstruments(limit, since));
    }

    public List<AnalyticsResponse> getOverbought(LocalDateTime since) {
        return mapAll(repo.findOverboughtInstruments(since));
    }

    public List<AnalyticsResponse> getOversold(LocalDateTime since) {
        return mapAll(repo.findOversoldInstruments(since));
    }

    // --- Private Functional Helpers ---

    private Predicate<AnalyticsSnapshot> applyFilters(AnalyticsQuery query) {
        return Stream.<Predicate<AnalyticsSnapshot>>of(optionalFilter(query.getIncludeOverbought(), AnalyticsSnapshot::isOverbought), optionalFilter(query.getIncludeOversold(), AnalyticsSnapshot::isOversold)).reduce(Predicate::and).orElse(snapshot -> true); // default: no filters
    }

    private Predicate<AnalyticsSnapshot> optionalFilter(Boolean condition, Predicate<AnalyticsSnapshot> filter) {
        return Boolean.TRUE.equals(condition) ? filter : snapshot -> true;
    }

    private AnalyticsResponse mapOrThrow(Optional<AnalyticsSnapshot> snapshotOpt, String errorMessage) {
        return snapshotOpt.map(mapper::toResponce).orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }

    private List<AnalyticsResponse> mapAll(List<AnalyticsSnapshot> snapshots) {
        return snapshots.stream().map(mapper::toResponce).toList();
    }


    public AnalyticsResponse calculateRealTimeAnalytics(Long instrumentId) {
        List<MarketData> historicalData = marketDataRepository.findRecentData(instrumentId); // adapter/port
        LocalDateTime now = LocalDateTime.now();

        AnalyticsSnapshot snapshot = marketAnalyticsService.calculateAnalytics(instrumentId, historicalData, now);
        return mapper.toResponce(snapshot);
    }

}
