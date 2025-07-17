package com.IntelStream.application.command.handler;

import com.IntelStream.application.command.dto.CreateInstrumentCommand;
import com.IntelStream.application.command.handler.command_mapper.InstrumentCommandMapper;
import com.IntelStream.application.common.exception.DuplicateInstrumentException;
import com.IntelStream.application.common.exception.InstrumentNotFoundException;
import com.IntelStream.application.port.out.EventPublisher;
import com.IntelStream.domain.event.InstrumentCreatedEvent;
import com.IntelStream.domain.event.InstrumentDeactivatedEvent;
import com.IntelStream.domain.model.Instrument;
import com.IntelStream.domain.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class InstrumentCommandHandler {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentCommandMapper instrumentMapper;
    private final EventPublisher eventPublisher;

    @Transactional
    public Long handle(CreateInstrumentCommand command) {
        validateDuplicateInstrument(command.getSymbol());

        Instrument instrument = instrumentMapper.toDomain(command);
        Instrument saved = instrumentRepository.save(instrument);

        publishInstrumentCreatedEvent(saved);

        return saved.getId();
    }

    @Transactional
    public void deactivateInstrument(Long instrumentId) {
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new InstrumentNotFoundException("Instrument not found with ID: " + instrumentId));

        instrumentRepository.deactivateInstrument(instrumentId);

        publishInstrumentDeactivatedEvent(instrument);
    }

    private void validateDuplicateInstrument(String symbol) {
        instrumentRepository.findBySymbol(symbol)
                .ifPresent(existing -> {
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

        log.info("Created instrument ID: {}", instrument.getId());
    }

    private void publishInstrumentDeactivatedEvent(Instrument instrument) {
        eventPublisher.publish(InstrumentDeactivatedEvent.builder()
                .instrumentId(instrument.getId())
                .symbol(instrument.getSymbol())
                .timestamp(LocalDateTime.now())
                .build());

        log.info("Deactivated instrument ID: {}", instrument.getId());
    }
}
