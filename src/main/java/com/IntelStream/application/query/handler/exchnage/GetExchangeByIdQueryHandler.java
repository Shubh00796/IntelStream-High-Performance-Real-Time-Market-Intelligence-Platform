package com.IntelStream.application.query.handler.exchnage;

import com.IntelStream.application.query.service.exchange.ExchangeQueryService;
import com.IntelStream.presentation.dto.response.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetExchangeByIdQueryHandler {

    private final ExchangeQueryService service;

    public ExchangeResponse handle(Long id) {
        return service.getById(id);
    }
}
