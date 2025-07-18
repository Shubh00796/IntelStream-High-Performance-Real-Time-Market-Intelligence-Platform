package com.IntelStream.application.query.handler.instruments;

import com.IntelStream.application.query.dto.InstrumentSearchQuery;
import com.IntelStream.application.query.service.instrument.InstrumentQueryService;
import com.IntelStream.presentation.dto.response.InstrumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchInstrumentsQueryHandler {
    private final InstrumentQueryService svc;

    public List<InstrumentResponse> handle(InstrumentSearchQuery query) {
        return svc.search(query);
    }
}

