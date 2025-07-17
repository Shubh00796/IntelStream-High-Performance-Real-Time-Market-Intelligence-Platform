package com.IntelStream.application.command.command_mapper;

import com.IntelStream.application.command.dto.UpdateMarketDataCommand;
import com.IntelStream.domain.model.MarketData;
import org.springframework.stereotype.Component;

@Component
public class MarketDataCommandMapper {
    public MarketData toDomain(UpdateMarketDataCommand cmd) {
        return MarketData.builder()
                .instrumentId(cmd.getInstrumentId())
                .exchangeId(cmd.getExchangeId())
                .price(cmd.getPrice())
                .volume(cmd.getVolume())
                .bidPrice(cmd.getBidPrice())
                .askPrice(cmd.getAskPrice())
                .timestamp(cmd.getTimestamp())
                .source(cmd.getSource())
                .build();
    }
}
