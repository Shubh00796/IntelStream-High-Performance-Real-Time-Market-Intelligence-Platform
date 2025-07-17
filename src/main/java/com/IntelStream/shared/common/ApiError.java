package com.IntelStream.shared.common;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@Jacksonized
public class ApiError {
    int status;
    String error;
    String message;
    String path;
    LocalDateTime timestamp;
    List<String> details;
}
