package com.IntelStream.application.command.handler.command_mapper;

import com.IntelStream.application.command.dto.CreateInstrumentCommand;
import com.IntelStream.domain.model.Instrument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InstrumentCommandMapper {
    public Instrument toDomain(CreateInstrumentCommand command) {
        return Instrument.builder()
                .symbol(command.getSymbol())
                .name(command.getName())
                .instrumentType(command.getInstrumentType())
                .sector(command.getSector())
                .exchangeId(command.getExchangeId())
                .currency(command.getCurrency())
                .active(command.getActive())
                .createdAt(LocalDateTime.now())
                .build();
    }
}

