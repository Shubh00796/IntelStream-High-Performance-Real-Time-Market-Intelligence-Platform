package com.IntelStream.presentation.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@Jacksonized
public class InstrumentResponse {
    Long id;
    String symbol;
    String name;
    String instrumentType; // STOCK, CRYPTO, FOREX, COMMODITY
    String sector;
    Long exchangeId;
    String exchangeCode;
    String currency;
    Boolean active;


    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
