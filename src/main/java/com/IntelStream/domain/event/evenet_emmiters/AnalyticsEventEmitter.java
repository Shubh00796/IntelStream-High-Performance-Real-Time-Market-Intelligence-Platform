package com.IntelStream.domain.event.evenet_emmiters;

import com.IntelStream.application.port.out.EventPublisher;
import com.IntelStream.domain.event.AnalyticsGeneratedEvent;
import com.IntelStream.domain.model.AnalyticsSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsEventEmitter {

    private final EventPublisher eventPublisher;

    public void emitGenerated(AnalyticsSnapshot snapshot) {
        eventPublisher.publish(AnalyticsGeneratedEvent.builder()
                .analyticsId(snapshot.getId())
                .instrumentId(snapshot.getInstrumentId())
                .rsi(snapshot.getRsi())
                .volatility(snapshot.getVolatility())
                .calculatedAt(snapshot.getCalculatedAt())
                .build());
    }
}
