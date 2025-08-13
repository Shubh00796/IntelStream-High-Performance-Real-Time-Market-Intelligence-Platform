package com.IntelStream.presentation.dto.response;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Jacksonized
public class ExchangeResponse {
    Long id;
    String code;         // e.g., NASDAQ, NSE
    String name;         // Full exchange name
    String timezone;     // System timezone handling
    Boolean active;

}
