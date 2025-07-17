package com.IntelStream.application.command.handler;

import com.IntelStream.application.command.command_mapper.MarketDataCommandMapper;
import com.IntelStream.application.command.dto.BulkUpdateMarketDataCommand;
import com.IntelStream.application.command.dto.UpdateMarketDataCommand;
import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.domain.event.evenet_emmiters.MarketDataEventEmitter;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.repository.InstrumentRepository;
import com.IntelStream.infrastructure.persistence.repository.impl.MarketDataRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDataCommandHandler {

    private final MarketDataRepositoryImpl marketDataRepository;
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

    @Async
    public CompletableFuture<Integer> handleBulkAsync(BulkUpdateMarketDataCommand cmd) {
        return CompletableFuture.supplyAsync(() -> handleBulk(cmd));
    }

    @Transactional
    public int handleBulk(BulkUpdateMarketDataCommand cmd) {
        log.info("Processing bulk update: {}", cmd.getMarketDataList().size());

        List<MarketData> dataList = cmd.getMarketDataList().stream()
                .map(marketDataMapper::toDomain)
                .toList();

        int count = marketDataRepository.bulkInsert(dataList);

        eventEmitter.emitBulkProcessed(cmd.getBatchId(), count, cmd.getProcessingTimestamp());
        log.info("Bulk insert completed. Total: {}", count);

        return count;
    }


    // 🔐 Private helper method (replaces MarketDataValidator class)
    private void validateInstrumentExists(Long instrumentId) {
        instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found with ID: " + instrumentId));
    }
}
