package com.IntelStream.application.query.service.instrument;

import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.application.query.dto.InstrumentSearchQuery;
import com.IntelStream.domain.model.Instrument;
import com.IntelStream.domain.repository.InstrumentRepository;
import com.IntelStream.infrastructure.persistence.mapper.InstrumentMapper;
import com.IntelStream.presentation.dto.response.InstrumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentQueryService {
    private final InstrumentRepository repo;
    private final InstrumentMapper mapper;

    public InstrumentResponse getById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found: " + id));
    }

    // In InstrumentQueryService.java
    public List<InstrumentResponse> getByExchangeIdPaged(Long exchangeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByExchangeId(exchangeId, pageable)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }



    public InstrumentResponse getBySymbol(String symbol) {
        return repo.findBySymbol(symbol)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found: " + symbol));
    }

    public List<InstrumentResponse> search(InstrumentSearchQuery q) {
        List<Instrument> instruments = fetchInstruments(q);

        Predicate<Instrument> filters = InstrumentFilter.bySector(q.getSector())
                .and(InstrumentFilter.byActiveOnly(q.getActiveOnly()));

        return instruments.stream()
                .filter(filters)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    private List<Instrument> fetchInstruments(InstrumentSearchQuery q) {
        return new QuerySelector<List<Instrument>>()
                .when(v -> hasText(q.getKeyword()), () -> repo.findByNameContainingIgnoreCase(q.getKeyword(), PageRequest.of(0, 50)).getContent())
                .when(v -> q.getInstrumentType() != null, () -> repo.findByInstrumentType(q.getInstrumentType()))
                .orElse(() -> q.getActiveOnly()
                        ? repo.findActiveInstruments()
                        : repo.findByInstrumentType(null));
    }


    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
