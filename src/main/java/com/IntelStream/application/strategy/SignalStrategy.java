package com.IntelStream.application.strategy;


import java.math.BigDecimal;

public interface SignalStrategy {
    String generateSignal(BigDecimal rsi);
}
