package com.IntelStream.application.query.handler.exchnage;


import com.IntelStream.application.query.service.exchange.ExchangeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class IsMarketOpenQueryHandler {

    private final ExchangeQueryService service;

    public boolean handle(Long exchangeId, LocalDateTime currentTime) {
        return service.isMarketOpen(exchangeId, currentTime);
    }
}
