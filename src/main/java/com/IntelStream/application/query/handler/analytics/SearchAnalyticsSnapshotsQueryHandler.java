package com.IntelStream.application.query.handler.analytics;


import com.IntelStream.application.query.dto.AnalyticsQuery;
import com.IntelStream.presentation.dto.response.AnalyticsResponse;
import com.IntelStream.application.query.service.analytics.AnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchAnalyticsSnapshotsQueryHandler {

    private final AnalyticsQueryService service;

    public List<AnalyticsResponse> handle(AnalyticsQuery query) {
        return service.search(query);
    }
}
