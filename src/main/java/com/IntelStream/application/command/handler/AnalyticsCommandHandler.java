package com.IntelStream.application.command.handler;

import com.IntelStream.application.command.command_mapper.AnalyticsCommandMapper;
import com.IntelStream.domain.event.evenet_emmiters.AnalyticsEventEmitter;
import com.IntelStream.domain.repository.AnalyticsRepository;
import com.IntelStream.domain.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsCommandHandler {
    private final AnalyticsRepository analyticsRepository;
    private final MarketAnalyticsService analyticsService;
    private final AnalyticsCommandMapper commandMapper;
    private final AnalyticsEventEmitter eventEmitter;
    private final MarketDataRepository marketDataRepository;

}
