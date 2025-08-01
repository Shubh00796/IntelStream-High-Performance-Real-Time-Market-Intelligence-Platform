package com.IntelStream.application.processor;

import com.IntelStream.domain.model.AnalyticsSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class AnalyticsSnapshotProcessor {

    private final List<AnalyticsSnapshot> snapshots;

    //1. Filter Overbrought Snapshots
    public List<AnalyticsSnapshot> filterOverBroughtSnapshots() {
        return snapshots
                .stream()
                .filter(analyticsSnapshot -> analyticsSnapshot.isOverbought())
                .collect(Collectors.toList());
    }


}
