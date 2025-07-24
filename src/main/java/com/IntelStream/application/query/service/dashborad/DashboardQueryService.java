package com.IntelStream.application.query.service.dashborad;


import com.IntelStream.application.query.dto.*;
import com.IntelStream.domain.model.*;
import com.IntelStream.domain.repository.*;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public DashboardResponse getDashboardData(DashboardQuery query) {
        List<Long> instrumentIds = query.getInstrumentIds();
        List<Instrument> instruments = fetchInstruments(instrumentIds);
        Map<Long, MarketData> latestPrices = query.getIncludeLatestPrice()
                ? fetchLatestPrices(instrumentIds)
                : Collections.emptyMap();

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
  return analyticsRepository.
    }
