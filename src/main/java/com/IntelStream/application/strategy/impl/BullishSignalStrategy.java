package com.IntelStream.application.strategy.impl;

import com.IntelStream.application.strategy.SignalStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("bullish")
public class BullishSignalStrategy implements SignalStrategy {
    @Override
    public String generateSignal(BigDecimal rsi) {
        return rsi.compareTo(BigDecimal.valueOf(30)) > 0 ? "BUY" : "HOLD";
    }
}
