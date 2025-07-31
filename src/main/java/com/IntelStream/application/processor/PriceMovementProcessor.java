package com.IntelStream.application.processor;

import com.IntelStream.domain.model.PriceMovement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    // 3. Group by InstrumentId
    public Map<Long, List<PriceMovement>> groupByInstrumentId() {
        return priceMovements.stream()
                .collect(Collectors.groupingBy(PriceMovement::getInstrumentId));
    }

    //4. Group by InstrumentId and CurrentPrice
    public Map<Long, Map<BigDecimal, List<PriceMovement>>> groupByInstrumentIdAndCurrentPrice() {
        return priceMovements.stream()
                .collect(Collectors.groupingBy(PriceMovement::getInstrumentId,
                        Collectors.groupingBy(PriceMovement::getCurrentPrice))
                );
    }

    //5. Filter by InstrumentId And CurrentPrice
    public List<PriceMovement> filterByInstrumentIdAndCurrentPrice(Long instrumentId, BigDecimal currentPrice) {
        return priceMovements.stream()
                .filter(priceMovement -> priceMovement.getInstrumentId().equals(instrumentId) &&
                        priceMovement.getCurrentPrice().compareTo(currentPrice) == 0)
                .collect(Collectors.toList());
    }

    //6. Get Average Change Percentage by InstrumentId
    public Map<Long, BigDecimal> getAverageChangePercentageByInstrumentId() {
        return priceMovements.stream()
                .collect(Collectors.groupingBy(PriceMovement::getInstrumentId,
                        Collectors.averagingDouble(pm -> pm.getChangePercent().doubleValue())))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> BigDecimal.valueOf(entry.getValue())));
    }


}
