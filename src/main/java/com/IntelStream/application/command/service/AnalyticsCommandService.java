package com.IntelStream.application.command.service;


import com.IntelStream.application.command.dto.GenerateAnalyticsCommand;
import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.domain.event.evenet_emmiters.AnalyticsEventEmitter;
import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.repository.AnalyticsRepository;
import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.domain.service.analytics.MarketAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsCommandService {

    private final AnalyticsRepository analyticsRepository;
    private final MarketDataRepository marketDataRepository;
    private final MarketAnalyticsService analyticsService;
    private final AnalyticsEventEmitter analyticsEventEmitter;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long generateAnalytics(GenerateAnalyticsCommand command) {
        log.info("Generating analytics for instrument ID: {}", command.getInstrumentId());

        LocalDateTime start = command.getCalculatedAt().minusDays(60);
        LocalDateTime end = command.getCalculatedAt();

        List<MarketData> historical = marketDataRepository
                .findByInstrumentIdAndTimestampBetween(command.getInstrumentId(), start, end)
                .toList();

        if (historical.isEmpty()) {
            log.warn("No historical data found for instrument ID: {}", command.getInstrumentId());
            throw new ResourceNotFoundException("No historical market data available for instrument ID: " + command.getInstrumentId());
        }

        AnalyticsSnapshot snapshot = analyticsService.calculateAnalytics(
                command.getInstrumentId(),
                historical,
                command.getCalculatedAt()
        );

        AnalyticsSnapshot saved = analyticsRepository.save(snapshot);
        analyticsEventEmitter.emitGenerated(saved);

        log.info("Analytics snapshot saved with ID: {}", saved.getId());
        return saved.getId();
    }
}
