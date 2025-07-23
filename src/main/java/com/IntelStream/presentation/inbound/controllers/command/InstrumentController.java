package com.IntelStream.presentation.inbound.controllers.command;

import com.IntelStream.application.command.dto.CreateInstrumentCommand;
import com.IntelStream.application.command.handler.InstrumentCommandHandler;
import com.IntelStream.shared.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/instruments")
@RequiredArgsConstructor
public class InstrumentController {

    private final InstrumentCommandHandler instrumentCommandHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createInstrument(@RequestBody CreateInstrumentCommand command) {
        Long id = instrumentCommandHandler.handle(command);
        return ResponseEntity.ok(ApiResponse.ok(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateInstrument(@PathVariable Long id) {
        instrumentCommandHandler.handleDeactivate(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
