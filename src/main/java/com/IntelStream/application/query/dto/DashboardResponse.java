package com.IntelStream.application.query.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class DashboardResponse {
    LocalDateTime asOf;
    List<InstrumentDashboardData> instruments;
}