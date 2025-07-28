package com.IntelStream.presentation.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ExchangeResponse {
    Long id;
    String code;         // e.g., NASDAQ, NSE
    String name;         // Full exchange name
    String timezone;     // System timezone handling
    Boolean active;

}
