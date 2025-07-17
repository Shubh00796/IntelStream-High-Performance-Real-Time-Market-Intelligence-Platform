package com.IntelStream.application.command.handler;

import com.IntelStream.application.command.dto.UpdateMarketDataCommand;
import com.IntelStream.application.command.handler.command_mapper.MarketDataCommandMapper;
import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.domain.event.evenet_emmiters.MarketDataEventEmitter;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.repository.InstrumentRepository;
import com.IntelStream.domain.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDataCommandHandler {

    private final MarketDataRepository marketDataRepository;
    private final InstrumentRepository instrumentRepository;
    private final MarketDataCommandMapper marketDataMapper;
    private final MarketDataEventEmitter eventEmitter;

    @Transactional
    public Long handle(UpdateMarketDataCommand command) {
        log.debug("Updating market data for instrument ID: {}", command.getInstrumentId());
        MarketData marketData = marketDataMapper.toDomain(command);
        MarketData savedMarketData = marketDataRepository.save(marketData);

        eventEmitter.emitUpdate(savedMarketData);
        log.info("Saved market data ID: {}", savedMarketData.getId());

        return savedMarketData.getId();
    }


    // 🔐 Private helper method (replaces MarketDataValidator class)
    private void validateInstrumentExists(Long instrumentId) {
        instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found with ID: " + instrumentId));
    }
}
