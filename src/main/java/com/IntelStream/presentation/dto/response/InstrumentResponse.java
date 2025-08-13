package com.IntelStream.presentation.dto.response;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
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
