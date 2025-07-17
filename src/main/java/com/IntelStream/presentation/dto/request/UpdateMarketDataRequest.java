package com.IntelStream.presentation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class UpdateMarketDataRequest {

    @NotNull
    @Positive
    Long instrumentId;

    @NotNull
    @Positive
    Long exchangeId;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 10, fraction = 8)
    BigDecimal price;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 10, fraction = 8)
    BigDecimal volume;

    @Digits(integer = 10, fraction = 8)
    BigDecimal bidPrice;

    @Digits(integer = 10, fraction = 8)
    BigDecimal askPrice;

    @NotNull
    @PastOrPresent
    LocalDateTime timestamp;

    @Size(max = 50)
    String source;
}
