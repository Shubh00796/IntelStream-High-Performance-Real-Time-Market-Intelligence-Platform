package com.IntelStream.domain.service.sentiment;


import com.IntelStream.domain.model.NewsSentiment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Analyzes news sentiment using Java 8 functional idioms.
 */
public interface NewsSentimentService {

    List<NewsSentiment> filterPositive(List<NewsSentiment> news);

    List<NewsSentiment> filterNegative(List<NewsSentiment> news);

    Map<String, Long> countBySource(List<NewsSentiment> news);

    Map<String, Double> averageSentimentBySymbol(List<NewsSentiment> news);

    List<String> topNMostPositiveHeadlines(List<NewsSentiment> news, int n);

    List<String> topNMostNegativeHeadlines(List<NewsSentiment> news, int n);


    Optional<NewsSentiment> mostRecentPositive(List<NewsSentiment> news);

    List<String> getDistinctSymbols(List<NewsSentiment> news);

    boolean anyExtremeNegative(List<NewsSentiment> news);

    Map<NewsSentiment.SentimentType, List<NewsSentiment>> groupBySentimentType(List<NewsSentiment> news);

    List<NewsSentiment> filterByTimeRange(List<NewsSentiment> news, LocalDateTime from, LocalDateTime to);
}
