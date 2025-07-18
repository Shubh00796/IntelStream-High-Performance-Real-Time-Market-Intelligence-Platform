package com.IntelStream.application.query.service.analytics;

import com.IntelStream.application.query.dto.AnalyticsQuery;
import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.domain.repository.AnalyticsRepository;
import com.IntelStream.infrastructure.persistence.mapper.AnalyticsSnapshotMapper;
import com.IntelStream.presentation.dto.response.AnalyticsResponse;
import lombok.RequiredArgsConstructor;
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

    public AnalyticsResponse getById(Long id) {
        return mapOrThrow(repo.findById(id), "AnalyticsSnapshot not found with ID: " + id);
    }

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
}
