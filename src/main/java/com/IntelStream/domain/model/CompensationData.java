package com.IntelStream.domain.model;


import lombok.*;

import java.util.List;

@Value
@Builder
public class CompensationData {
    String applicationId;
    String stepFailed;
    String reason;
    List<String> completedSteps;
}
