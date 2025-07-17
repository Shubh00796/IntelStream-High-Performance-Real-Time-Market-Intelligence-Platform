package com.IntelStream.application.query.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class InstrumentSearchQuery {

    String keyword;

    @Pattern(regexp = "STOCK|CRYPTO|FOREX|COMMODITY")
    String instrumentType;

    String sector;

    @Builder.Default
    Boolean activeOnly = true;
}
