package com.IntelStream.domain.service.sentiment;

import com.IntelStream.domain.model.NewsSentiment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NewsSentimentServiceImpl implements NewsSentimentService {
    @Override
    public List<NewsSentiment> filterPositive(List<NewsSentiment> news) {
        return news
                .stream()
                .filter(NewsSentiment::isPositive)
                .collect(Collectors.toList());
    }

    @Override
    public List<NewsSentiment> filterNegative(List<NewsSentiment> news) {
        return news
                .stream()
                .filter(NewsSentiment::isNegative)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> countBySource(List<NewsSentiment> news) {
        return news.stream()
                .collect(Collectors.groupingBy(NewsSentiment::getSource, Collectors.counting()));
    }

    @Override
    public Map<String, Double> averageSentimentBySymbol(List<NewsSentiment> news) {
        return news.stream()
                .collect(Collectors.groupingBy(
                        NewsSentiment::getSymbol,
                        Collectors.averagingDouble(NewsSentiment::getSentimentScore)
                ));
    }

    @Override
    public List<String> topNMostPositiveHeadlines(List<NewsSentiment> news, int n) {
        return news
                .stream()
                .filter(NewsSentiment::isPositive)
                .sorted(Comparator.comparing(NewsSentiment::getSentimentScore).reversed())
                .limit(n)
                .map(NewsSentiment::getHeadline)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> topNMostNegativeHeadlines(List<NewsSentiment> news, int n) {
        return news
                .stream()
                .filter(NewsSentiment::isNegative)
                .sorted(Comparator.comparing(NewsSentiment::getSentimentScore).reversed())
                .limit(n)
                .map(NewsSentiment::getHeadline)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NewsSentiment> mostRecentPositive(List<NewsSentiment> news) {
        return news
                .stream()
                .filter(NewsSentiment::isPositive)
                .max(Comparator.comparing(NewsSentiment::getPublishedAt));
    }

    @Override
    public List<String> getDistinctSymbols(List<NewsSentiment> news) {
        return news
                .stream()
                .map(NewsSentiment::getSymbol)
                .distinct()
                .toList();
    }

    @Override
    public boolean anyExtremeNegative(List<NewsSentiment> news) {
        return news
                .stream()
                .anyMatch(newsSentiment -> newsSentiment.getSentimentScore() < -0.7);
    }

    @Override
    public Map<NewsSentiment.SentimentType, List<NewsSentiment>> groupBySentimentType(List<NewsSentiment> news) {
        return news
                .stream()
                .collect(Collectors.groupingBy(NewsSentiment::getSentimentType));
    }

    @Override
    public List<NewsSentiment> filterByTimeRange(List<NewsSentiment> news, LocalDateTime from, LocalDateTime to) {
        return news
                .stream()
                .filter(newsSentiment -> !newsSentiment.getPublishedAt().isBefore(from) && !newsSentiment.getPublishedAt().isAfter(to))
                .collect(Collectors.toList());
    }
}
