package com.IntelStream.infrastructure.messaging.consumer;

import com.IntelStream.domain.event.MarketDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MarketDataEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(MarketDataEventConsumer.class);

    private static final Map<String, Consumer<MarketDataEvent>> EVENT_HANDLERS;

    static {
        Map<String, Consumer<MarketDataEvent>> handlers = new HashMap<>();
        handlers.put("PRICE_UPDATE", MarketDataEventConsumer::logPriceUpdate);
        handlers.put("VOLUME_SPIKE", MarketDataEventConsumer::logVolumeSpike);
        EVENT_HANDLERS = Collections.unmodifiableMap(handlers);
    }

    public void consumeMarketDataEvent(MarketDataEvent event, String topic, int partition, long offset) {
        if (event == null) {
            log.warn("Received null MarketDataEvent (topic={}, partition={}, offset={})", topic, partition, offset);
            return;
        }

        log.info("📥 Received MarketDataEvent: instrumentId={}, eventType={}, topic={}, partition={}, offset={}",
                event.getInstrumentId(), event.getEventType(), topic, partition, offset);

        EVENT_HANDLERS
                .getOrDefault(event.getEventType(), MarketDataEventConsumer::logUnknownEventType)
                .accept(event);
    }

    private static void logPriceUpdate(MarketDataEvent event) {
        log.info("💰 Price update: instrumentId={}, price={}", event.getInstrumentId(), event.getPrice());
    }

    private static void logVolumeSpike(MarketDataEvent event) {
        log.info("📈 Volume spike: instrumentId={}, volume={}", event.getInstrumentId(), event.getVolume());
    }

    private static void logUnknownEventType(MarketDataEvent event) {
        log.warn("Unknown event type: {}", event.getEventType());
    }
}
