package com.IntelStream.domain.event.evenet_emmiters;

import com.IntelStream.application.port.out.EventPublisher;
import com.IntelStream.domain.event.BulkMarketDataProcessedEvent;
import com.IntelStream.domain.event.MarketDataUpdatedEvent;
import com.IntelStream.domain.model.MarketData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MarketDataEventEmitter {

    private final EventPublisher eventPublisher;

    public void emitUpdate(MarketData data) {
        eventPublisher.publish(MarketDataUpdatedEvent.builder()
                .marketDataId(data.getId())
                .instrumentId(data.getInstrumentId())
                .price(data.getPrice())
                .volume(data.getVolume())
                .timestamp(data.getTimestamp())
                .build());
    }

    public void emitBulkProcessed(String batchId, int count, LocalDateTime processedAt) {
        eventPublisher.publish(BulkMarketDataProcessedEvent.builder()
                .batchId(batchId)
                .recordCount(count)
                .processingTimestamp(processedAt)
                .build());
    }
}
