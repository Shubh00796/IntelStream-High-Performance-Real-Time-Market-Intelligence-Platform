package com.IntelStream.presentation.inbound.controllers.command;

import com.IntelStream.application.command.dto.GenerateAnalyticsCommand;
import com.IntelStream.application.command.handler.AnalyticsCommandHandler;
import com.IntelStream.shared.common.ApiError;
import com.IntelStream.shared.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsCommandHandler analyticsCommandHandler;



    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<Long>> generateAnalytics(@RequestBody GenerateAnalyticsCommand command) {
        Long id = analyticsCommandHandler.handle(command);

        if (id == null) {
            // Analytics could not be generated (no historical data)
            return ResponseEntity
                    .status(404)
                    .body(ApiResponse.error(ApiError.builder()
                            .message("No historical data found for instrument ID: " + command.getInstrumentId())
                            .build()));
        }

        // Analytics created successfully
        return ResponseEntity
                .status(201)
                .body(ApiResponse.ok(id));
    }
}
