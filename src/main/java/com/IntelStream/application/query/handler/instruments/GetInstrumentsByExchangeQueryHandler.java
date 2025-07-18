package com.IntelStream.application.query.handler.instruments;

import com.IntelStream.application.query.service.instrument.InstrumentQueryService;
import com.IntelStream.presentation.dto.response.InstrumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetInstrumentsByExchangeQueryHandler {

    private final InstrumentQueryService svc;

    public List<InstrumentResponse> handle(Long exchangeId, int page, int size) {
        return svc.getByExchangeIdPaged(exchangeId, page, size);
    }
}
