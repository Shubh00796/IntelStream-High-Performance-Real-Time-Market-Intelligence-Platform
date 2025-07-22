package com.IntelStream.application.query.handler.exchnage;


import com.IntelStream.application.query.service.exchange.ExchangeQueryService;
import com.IntelStream.presentation.dto.response.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetExchangesByCurrencyQueryHandler {

    private final ExchangeQueryService service;

    public List<ExchangeResponse> handle(String currency) {
        return service.getByCurrency(currency);
    }
}
