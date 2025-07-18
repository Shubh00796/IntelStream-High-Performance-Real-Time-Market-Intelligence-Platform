package com.IntelStream.application.command.handler;

import com.IntelStream.application.command.dto.GenerateAnalyticsCommand;
import com.IntelStream.domain.event.evenet_emmiters.AnalyticsEventEmitter;
import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.repository.AnalyticsRepository;
import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.domain.service.analytics.MarketAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsCommandHandler {

    private final AnalyticsRepository analyticsRepository;
    private final MarketDataRepository marketDataRepository;
    private final MarketAnalyticsService analyticsService;
    private final AnalyticsEventEmitter analyticsEventEmitter;

    @Transactional
    public Long handle(GenerateAnalyticsCommand command) {
        log.info("Generating analytics for instrument: {}", command.getInstrumentId());

        LocalDateTime start = command.getCalculatedAt().minusDays(60);
        LocalDateTime end = command.getCalculatedAt();

        List<MarketData> historical = marketDataRepository
                .findByInstrumentIdAndTimestampBetween(command.getInstrumentId(), start, end)
                .toList();

        if (historical.isEmpty()) {
            log.warn("No historical data found for instrument: {}", command.getInstrumentId());
            return null;
        }

        AnalyticsSnapshot snapshot = analyticsService.calculateAnalytics(
                command.getInstrumentId(),
                historical,
                command.getCalculatedAt()
        );

        AnalyticsSnapshot saved = analyticsRepository.save(snapshot);
        analyticsEventEmitter.emitGenerated(snapshot);



        log.info("Analytics snapshot saved with ID: {}", saved.getId());
        return saved.getId();
    }
}

