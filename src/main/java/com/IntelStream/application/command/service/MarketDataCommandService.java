package com.IntelStream.application.command.service;

import com.IntelStream.application.command.command_mapper.MarketDataCommandMapper;
import com.IntelStream.application.command.dto.BulkUpdateMarketDataCommand;
import com.IntelStream.application.command.dto.UpdateMarketDataCommand;
import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.domain.event.evenet_emmiters.MarketDataEventEmitter;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.repository.InstrumentRepository;
import com.IntelStream.domain.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketDataCommandService {

    private final MarketDataRepository marketDataRepository;
    private final InstrumentRepository instrumentRepository;
    private final MarketDataEventEmitter eventEmitter;
    private final MarketDataCommandMapper mapper;

    /**
     * Updates a single market data entry.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED) // ✅ Default and safe for most cases
    public Long updateMarketData(UpdateMarketDataCommand command) {
        log.debug("Starting update for instrument ID: {}", command.getInstrumentId());

        validateInstrumentExists(command.getInstrumentId());

        MarketData marketData = mapper.toDomain(command);
        MarketData saved = marketDataRepository.save(marketData);

        log.info("Market data saved with ID: {}", saved.getId());
        eventEmitter.emitUpdate(saved);

        return saved.getId();
    }

    /**
     * Performs bulk update of market data entries.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int bulkUpdateMarketData(BulkUpdateMarketDataCommand cmd) {
        log.info("Starting bulk update. Batch ID: {}, Entries: {}", cmd.getBatchId(), cmd.getMarketDataList().size());

        List<MarketData> dataList = cmd.getMarketDataList().stream()
                .map(mapper::toDomain)
                .toList();

        int count = marketDataRepository.bulkInsert(dataList);

        log.info("Bulk insert completed. Inserted: {}", count);
        eventEmitter.emitBulkProcessed(cmd.getBatchId(), count, cmd.getProcessingTimestamp());

        return count;
    }

    /**
     * Validates that the instrument exists before processing.
     */
    private void validateInstrumentExists(Long instrumentId) {
        boolean exists = instrumentRepository.findById(instrumentId).isPresent();
        if (!exists) {
            log.warn("Instrument not found with ID: {}", instrumentId);
            throw new ResourceNotFoundException("Instrument not found with ID: " + instrumentId);
        }
    }
}
