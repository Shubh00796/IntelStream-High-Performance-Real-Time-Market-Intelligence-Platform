package com.IntelStream.application.query.service.dashborad;

import com.IntelStream.application.query.dto.*;
import com.IntelStream.domain.model.*;
import com.IntelStream.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardQueryService {

    private final InstrumentRepository instrumentRepository;
    private final MarketDataRepository marketDataRepository;
    private final AnalyticsRepository analyticsRepository;

    /**
     * Main method to get dashboard data.
     */

    @Transactional(readOnly = true)
    public DashboardResponse getDashboardData(DashboardQuery query) {
        List<Long> instrumentIds = query.getInstrumentIds();

        List<Instrument> instruments = fetchInstruments(instrumentIds);

        Map<Long, MarketData> latestPrices = query.getIncludeLatestPrice()
                ? fetchLatestPrices(instrumentIds)
                : Collections.emptyMap();

        Map<Long, AnalyticsSnapshot> analyticsSnapshots = query.getIncludeAnalytics()
                ? fetchAnalyticsSnapshots(instrumentIds)
                : Collections.emptyMap();

        Map<Long, List<MarketData>> priceHistories = query.getIncludePriceHistory()
                ? fetchPriceHistories(instrumentIds, query.getAsOf(), query.getPriceHistoryHours())
                : Collections.emptyMap();

        List<InstrumentDashboardData> dashboardData = buildDashboardData(
                instruments, latestPrices, analyticsSnapshots, priceHistories
        );

        return buildDashboardResponse(query.getAsOf(), dashboardData);
    }

    /**
     * Fetches instruments from repository.
     */
    private List<Instrument> fetchInstruments(List<Long> instrumentIds) {
        return instrumentIds.stream()
                .map(instrumentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Fetches the latest prices for each instrument.
     */
    private Map<Long, MarketData> fetchLatestPrices(List<Long> instrumentIds) {
        return marketDataRepository.findLatestByInstrumentIds(instrumentIds).stream()
                .collect(Collectors.toMap(MarketData::getInstrumentId, md -> md));
    }

    /**
     * Fetches the latest analytics snapshot for each instrument.
     */
    private Map<Long, AnalyticsSnapshot> fetchAnalyticsSnapshots(List<Long> instrumentIds) {
        return instrumentIds.stream()
                .map(id -> analyticsRepository.findLatestByInstrumentId(id)
                        .map(snapshot -> Map.entry(id, snapshot)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Fetches price history for each instrument.
     */
    private Map<Long, List<MarketData>> fetchPriceHistories(List<Long> instrumentIds, LocalDateTime asOf, int hours) {
        LocalDateTime from = asOf.minusHours(hours);
        return instrumentIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> marketDataRepository.findByInstrumentIdAndTimestampBetween(
                                id, from, asOf
                        ).toList()
                ));
    }

    /**
     * Builds dashboard data for each instrument.
     */
    private List<InstrumentDashboardData> buildDashboardData(
            List<Instrument> instruments,
            Map<Long, MarketData> latestPrices,
            Map<Long, AnalyticsSnapshot> analyticsSnapshots,
            Map<Long, List<MarketData>> priceHistories
    ) {
        return instruments.stream()
                .map(instrument -> InstrumentDashboardData.builder()
                        .instrument(instrument)
                        .latestPrice(latestPrices.get(instrument.getId()))
                        .analyticsSnapshot(analyticsSnapshots.get(instrument.getId()))
                        .priceHistory(priceHistories.getOrDefault(instrument.getId(), List.of()))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Builds the final dashboard response.
     */
    private DashboardResponse buildDashboardResponse(LocalDateTime asOf, List<InstrumentDashboardData> dashboardData) {
        return DashboardResponse.builder()
                .asOf(asOf)
                .instruments(dashboardData)
                .build();
    }
}