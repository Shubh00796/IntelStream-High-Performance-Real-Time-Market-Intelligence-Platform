package com.IntelStream.domain.service.economy;


import com.IntelStream.domain.model.EconomicIndicator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EconomicIndicatorService {

    List<EconomicIndicator> filterHighInflation(List<EconomicIndicator> indicators);

    List<EconomicIndicator> negativeGDPGrowth(List<EconomicIndicator> indicators);

    Map<String, List<EconomicIndicator>> groupByCountry(List<EconomicIndicator> indicators);

    Map<EconomicIndicator.IndicatorType, Double> averageValueByType(List<EconomicIndicator> indicators);

    List<String> countriesWithRateCut(List<EconomicIndicator> indicators);

    List<EconomicIndicator> sortByDateDesc(List<EconomicIndicator> indicators);

    Optional<EconomicIndicator> latestIndicatorOfType(List<EconomicIndicator> indicators, EconomicIndicator.IndicatorType type);

    List<EconomicIndicator> filterByDateRange(List<EconomicIndicator> indicators, LocalDate from, LocalDate to);

    Map<String, Double> latestGDPByCountry(List<EconomicIndicator> indicators);

    boolean allInflationBelowThreshold(List<EconomicIndicator> indicators, double threshold);
}
