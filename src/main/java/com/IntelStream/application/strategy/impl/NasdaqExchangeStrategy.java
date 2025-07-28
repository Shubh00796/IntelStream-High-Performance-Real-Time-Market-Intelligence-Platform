package com.IntelStream.application.strategy.impl;


import com.IntelStream.application.strategy.ExchangeStrategy;
import com.IntelStream.domain.model.Exchange;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component("NASDAQ")
public class NasdaqExchangeStrategy implements ExchangeStrategy {

    @Override
    public boolean isMarketOpen(Exchange exchange, LocalDateTime currentTime) {
        // Market hours logic: exclude weekends
        return currentTime.getDayOfWeek() != DayOfWeek.SATURDAY &&
                currentTime.getDayOfWeek() != DayOfWeek.SUNDAY &&
                exchange.isMarketOpen(currentTime);
    }
}
