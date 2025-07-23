package com.IntelStream.application.command.handler;

import com.IntelStream.application.command.dto.CreateInstrumentCommand;
import com.IntelStream.application.command.service.InstrumentCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstrumentCommandHandler {

    private final InstrumentCommandService service;

    public Long handle(CreateInstrumentCommand command) {
        return service.createInstrument(command);
    }

    public void handleDeactivate(Long instrumentId) {
        service.deactivateInstrument(instrumentId);
    }
}
