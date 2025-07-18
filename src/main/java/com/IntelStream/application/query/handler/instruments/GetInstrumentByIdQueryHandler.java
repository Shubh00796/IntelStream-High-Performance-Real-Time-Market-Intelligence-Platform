package com.IntelStream.application.query.handler.instruments;

import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.domain.repository.InstrumentRepository;
import com.IntelStream.infrastructure.persistence.mapper.InstrumentMapper;
import com.IntelStream.presentation.dto.response.InstrumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GetInstrumentByIdQueryHandler {

    private final InstrumentRepository repository;
    private final InstrumentMapper mapper;

    public InstrumentResponse handle(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found"));
    }
}
