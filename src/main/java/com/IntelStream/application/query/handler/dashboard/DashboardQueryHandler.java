package com.IntelStream.application.query.handler.dashboard;

import com.IntelStream.application.query.dto.DashboardQuery;
import com.IntelStream.application.query.dto.DashboardResponse;
import com.IntelStream.application.query.service.dashborad.DashboardQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardQueryHandler {

    private final DashboardQueryService dashboardQueryService;

    public DashboardResponse handle(DashboardQuery query) {
        return dashboardQueryService.getDashboardData(query);
    }
}
