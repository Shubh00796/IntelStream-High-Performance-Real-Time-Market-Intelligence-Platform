package com.IntelStream.application.query.handler.instruments;

import com.IntelStream.application.query.service.instrument.InstrumentQueryService;
import com.IntelStream.presentation.dto.response.InstrumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetInstrumentBySymbolQueryHandler {
    private final InstrumentQueryService svc;

    public InstrumentResponse handle(String symbol) {
        return svc.getBySymbol(symbol);
    }
}
