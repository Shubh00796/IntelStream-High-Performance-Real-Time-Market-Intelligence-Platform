package com.IntelStream.application.query.handler.analytics;


import com.IntelStream.application.query.service.analytics.AnalyticsQueryService;
import com.IntelStream.presentation.dto.response.AnalyticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculateRealTimeAnalyticsQueryHandler {

    private final AnalyticsQueryService service;

    public AnalyticsResponse handle(Long instrumentId) {
        return service.calculateRealTimeAnalytics(instrumentId);
    }
}
