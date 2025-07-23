    package com.IntelStream.application.command.handler;

    import com.IntelStream.application.command.dto.GenerateAnalyticsCommand;
    import com.IntelStream.application.command.service.AnalyticsCommandService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Component;

    @Component
    @RequiredArgsConstructor
    public class AnalyticsCommandHandler {

        private final AnalyticsCommandService service;

        public Long handle(GenerateAnalyticsCommand command) {
            return service.generateAnalytics(command);
        }
    }
