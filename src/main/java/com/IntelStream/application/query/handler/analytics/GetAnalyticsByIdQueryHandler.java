package com.IntelStream.application.query.handler.analytics;


import com.IntelStream.presentation.dto.response.AnalyticsResponse;
import com.IntelStream.application.query.service.analytics.AnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAnalyticsByIdQueryHandler {

    private final AnalyticsQueryService service;

    public AnalyticsResponse handle(Long id) {
        return service.getById(id);
    }
}
