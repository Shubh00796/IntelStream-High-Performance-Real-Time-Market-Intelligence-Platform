package com.IntelStream.application.command.handler;

import com.IntelStream.application.command.service.ExchangeCommandService;
import com.IntelStream.domain.model.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeCommandHandler {

    private final ExchangeCommandService service;

    public Long handleCreate(Exchange exchange) {
        return service.createExchange(exchange);
    }

    public void handleDeactivate(Long exchangeId) {
        service.deactivateExchange(exchangeId);
    }
}
