package com.IntelStream.application.processor;

import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.model.PriceMovement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PriceMovementProcessor {

    private final List<PriceMovement> priceMovements;


    // 1. Filter significant price movements
    public List<PriceMovement> filterSignificantMovements(List<PriceMovement> priceMovements, BigDecimal threshold) {
        return priceMovements.stream()
                .filter(priceMovement -> priceMovement.isSignificant(threshold))
                .collect(Collectors.toList());
    }

    //2. Sort by Highest Change Percentage
    public List<PriceMovement> sortByHighestChangePercentage(List<PriceMovement> priceMovements) {
        return priceMovements.stream()
                .sorted(Comparator.comparing(PriceMovement::getChangePercent).reversed())
                .collect(Collectors.toList());
    }

}
