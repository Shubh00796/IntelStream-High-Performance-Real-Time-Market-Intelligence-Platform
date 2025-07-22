package com.IntelStream.application.query.handler.exchnage;


import com.IntelStream.application.query.service.exchange.ExchangeQueryService;
import com.IntelStream.presentation.dto.response.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOpenMarketsAtQueryHandler {

    private final ExchangeQueryService service;

    public List<ExchangeResponse> handle(LocalDateTime currentTime) {
        return service.getOpenMarketsAt(currentTime);
    }
}
