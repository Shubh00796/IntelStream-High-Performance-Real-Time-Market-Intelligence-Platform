package com.IntelStream.application.command.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreateInstrumentCommand {

    @NotBlank(message = "Symbol is required")
    @Size(max = 20, message = "Symbol cannot exceed 20 characters")
    String symbol;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    String name;

    @NotBlank(message = "Instrument type is required")
    @Pattern(regexp = "STOCK|CRYPTO|FOREX|COMMODITY", message = "Invalid instrument type")
    String instrumentType;

    @Size(max = 100, message = "Sector cannot exceed 100 characters")
    String sector;

    @NotNull(message = "Exchange ID is required")
    @Positive(message = "Exchange ID must be positive")
    Long exchangeId;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 characters")
    String currency;

    @Builder.Default
    Boolean active = true;
}