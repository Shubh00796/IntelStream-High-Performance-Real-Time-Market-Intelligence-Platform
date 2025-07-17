package com.IntelStream.presentation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreateInstrumentRequest {

    @NotBlank(message = "Symbol is required")
    @Size(max = 20)
    String symbol;

    @NotBlank(message = "Name is required")
    @Size(max = 255)
    String name;

    @NotBlank(message = "Instrument type is required")
    @Pattern(regexp = "STOCK|CRYPTO|FOREX|COMMODITY")
    String instrumentType;

    @Size(max = 100)
    String sector;

    @NotNull
    @Positive
    Long exchangeId;

    @Size(min = 3, max = 3)
    String currency;

    @Builder.Default
    Boolean active = true;
}
