package com.IntelStream.application.command.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class UpdateMarketDataCommand {

    @NotNull(message = "Instrument ID is required")
    @Positive(message = "Instrument ID must be positive")
    Long instrumentId;

    @NotNull(message = "Exchange ID is required")
    @Positive(message = "Exchange ID must be positive")
    Long exchangeId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    @Digits(integer = 10, fraction = 8, message = "Price format invalid")
    BigDecimal price;

    @NotNull(message = "Volume is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Volume must be positive")
    @Digits(integer = 10, fraction = 8, message = "Volume format invalid")
    BigDecimal volume;

    @Digits(integer = 10, fraction = 8, message = "Bid price format invalid")
    BigDecimal bidPrice;

    @Digits(integer = 10, fraction = 8, message = "Ask price format invalid")
    BigDecimal askPrice;

    @NotNull(message = "Timestamp is required")
    @PastOrPresent(message = "Timestamp cannot be in the future")
    LocalDateTime timestamp;

    @Size(max = 50, message = "Source cannot exceed 50 characters")
    String source;
}