package com.IntelStream.application.command.service;


import com.IntelStream.application.command.command_mapper.InstrumentCommandMapper;
import com.IntelStream.application.command.dto.CreateInstrumentCommand;
import com.IntelStream.application.common.exception.DuplicateInstrumentException;
import com.IntelStream.application.common.exception.InstrumentNotFoundException;
import com.IntelStream.application.port.out.EventPublisher;
import com.IntelStream.domain.event.InstrumentCreatedEvent;
import com.IntelStream.domain.event.InstrumentDeactivatedEvent;
import com.IntelStream.domain.model.Instrument;
import com.IntelStream.domain.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstrumentCommandService {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentCommandMapper instrumentMapper;
    private final EventPublisher eventPublisher;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long createInstrument(CreateInstrumentCommand command) {
        validateDuplicateInstrument(command.getSymbol());

        Instrument instrument = instrumentMapper.toDomain(command);
        Instrument saved = instrumentRepository.save(instrument);

        publishInstrumentCreatedEvent(saved);
        log.info("Instrument created with ID: {}", saved.getId());

        return saved.getId();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @CacheEvict(cacheNames = {"instruments", "analytics"}, allEntries = true)
    public void deactivateInstrument(Long instrumentId) {
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new InstrumentNotFoundException("Instrument not found with ID: " + instrumentId));

        instrumentRepository.deactivateInstrument(instrumentId);
        publishInstrumentDeactivatedEvent(instrument);

        log.info("Instrument deactivated with ID: {}", instrumentId);
    }

    private void validateDuplicateInstrument(String symbol) {
        instrumentRepository.findBySymbol(symbol).ifPresent(existing -> {
            log.warn("Duplicate instrument found with symbol: {}", symbol);
            throw new DuplicateInstrumentException("Instrument with symbol '" + symbol + "' already exists");
        });
    }

    private void publishInstrumentCreatedEvent(Instrument instrument) {
        eventPublisher.publish(InstrumentCreatedEvent.builder()
                .instrumentId(instrument.getId())
                .symbol(instrument.getSymbol())
                .instrumentType(instrument.getInstrumentType())
                .timestamp(LocalDateTime.now())
                .build());
    }

    private void publishInstrumentDeactivatedEvent(Instrument instrument) {
        eventPublisher.publish(InstrumentDeactivatedEvent.builder()
                .instrumentId(instrument.getId())
                .symbol(instrument.getSymbol())
                .timestamp(LocalDateTime.now())
                .build());
    }
}

