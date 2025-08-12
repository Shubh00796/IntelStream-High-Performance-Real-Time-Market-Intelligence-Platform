package com.IntelStream.infrastructure.messaging.consumer;

import com.IntelStream.domain.event.MarketDataEvent;
import com.IntelStream.application.service.AnalyticsCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketDataEventConsumer {

    private final AnalyticsCalculationService analyticsService;

    @KafkaListener(
        topics = "market-data-events",
        groupId = "market-data-processor",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMarketDataEvent(
            @Payload MarketDataEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        
        log.info("📥 Received market data event: instrumentId={}, eventType={}, topic={}, partition={}, offset={}", 
            event.getInstrumentId(), event.getEventType(), topic, partition, offset);
        
        try {
            // Process based on event type
            switch (event.getEventType()) {
                case "PRICE_UPDATE":
                    handlePriceUpdate(event);
                    break;
                case "VOLUME_SPIKE":
                    handleVolumeSpike(event);
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("❌ Error processing market data event: {}", e.getMessage(), e);
            // In production, you might want to send to a dead letter queue
        }
    }

    private void handlePriceUpdate(MarketDataEvent event) {
        log.info("💰 Processing price update for instrument: {}, price: {}", 
            event.getInstrumentId(), event.getPrice());
        
        // Trigger analytics calculation
        analyticsService.calculateAnalytics(event.getInstrumentId());
    }

    private void handleVolumeSpike(MarketDataEvent event) {
        log.info("📈 Processing volume spike for instrument: {}, volume: {}", 
            event.getInstrumentId(), event.getVolume());
        
        // Trigger alert generation
        analyticsService.generateVolumeAlert(event.getInstrumentId(), event.getVolume());
    }
}
