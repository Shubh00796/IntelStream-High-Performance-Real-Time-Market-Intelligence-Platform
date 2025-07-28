package com.IntelStream.application.strategy.impl;

import com.IntelStream.application.strategy.SignalStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("neutral")
public class NeutralSignalStrategy implements SignalStrategy {
    @Override
    public String generateSignal(BigDecimal rsi) {
        return "HOLD";
    }
}
