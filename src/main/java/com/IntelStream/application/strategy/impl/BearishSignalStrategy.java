package com.IntelStream.application.strategy.impl;

import com.IntelStream.application.strategy.SignalStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component("bearish")
public class BearishSignalStrategy implements SignalStrategy {

    @Override
    public String generateSignal(BigDecimal rsi) {
        return rsi.compareTo(BigDecimal.valueOf(70)) < 0 ? "SELL" : "HOLD";
    }
}

