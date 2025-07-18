package com.IntelStream.application.query.handler.analytics;


import com.IntelStream.presentation.dto.response.AnalyticsResponse;
import com.IntelStream.application.query.service.analytics.AnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOverboughtAnalyticsQueryHandler {

    private final AnalyticsQueryService service;

    public List<AnalyticsResponse> handle(LocalDateTime since) {
        return service.getOverbought(since);
    }
}
