package com.IntelStream.domain.service.economy;

import com.IntelStream.domain.model.EconomicIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EconomicIndicatorServiceImpl implements EconomicIndicatorService {

    @Override
    public List<EconomicIndicator> filterHighInflation(List<EconomicIndicator> indicators) {
        return indicators
                .stream()
                .filter(EconomicIndicator::isHighInflation)
                .collect(Collectors.toList());
    }

    @Override
    public List<EconomicIndicator> negativeGDPGrowth(List<EconomicIndicator> indicators) {
        return indicators
                .stream()
                .filter(EconomicIndicator::isNegativeGrowth)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<EconomicIndicator>> groupByCountry(List<EconomicIndicator> indicators) {
        return indicators
                .stream().collect(Collectors.groupingBy(EconomicIndicator::getCountry));
    }

    @Override
    public Map<EconomicIndicator.IndicatorType, Double> averageValueByType(List<EconomicIndicator> indicators) {
        return indicators
                .stream()
                .collect(Collectors.groupingBy(EconomicIndicator::getType,
                        Collectors.averagingDouble(EconomicIndicator::getValue)

                ));
    }

    @Override
    public List<String> countriesWithRateCut(List<EconomicIndicator> indicators) {
        return indicators
                .stream()
                .filter(EconomicIndicator::isInterestRateCut)
                .map(EconomicIndicator::getCountry)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<EconomicIndicator> sortByDateDesc(List<EconomicIndicator> indicators) {
        return indicators
                .stream()
                .sorted(Comparator.comparing(EconomicIndicator::getDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EconomicIndicator> latestIndicatorOfType(List<EconomicIndicator> indicators, EconomicIndicator.IndicatorType type) {
        return indicators
                .stream()
                .filter(economicIndicator -> economicIndicator.getType() == type)
                .max(Comparator.comparing(EconomicIndicator::getDate));
    }

    @Override
    public List<EconomicIndicator> filterByDateRange(List<EconomicIndicator> indicators, LocalDate from, LocalDate to) {
        return indicators
                .stream()
                .filter(economicIndicator -> !economicIndicator.getDate().isBefore(from) && !economicIndicator.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Double> latestGDPByCountry(List<EconomicIndicator> indicators) {
        return indicators
                .stream()
                .collect(Collectors.groupingBy(EconomicIndicator::getCountry,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(EconomicIndicator::getDate)),
                                opt -> opt.map(EconomicIndicator::getValue).orElse(0.0))
                ));
    }

    @Override
    public boolean allInflationBelowThreshold(List<EconomicIndicator> indicators, double threshold) {
        return indicators.stream()
                .filter(i -> i.getType() == EconomicIndicator.IndicatorType.INFLATION)
                .allMatch(i -> i.getValue() < threshold);
    }
}
