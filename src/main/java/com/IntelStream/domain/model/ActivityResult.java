package com.IntelStream.domain.model;

// File: domain/model/ActivityResult.java

import lombok.*;

@Value
@Builder
public class ActivityResult {
    boolean success;
    String message;
    String stepName;
    Object data;
    String errorCode;
}
