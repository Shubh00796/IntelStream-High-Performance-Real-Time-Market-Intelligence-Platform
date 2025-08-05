package com.IntelStream.domain.model;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Represents sentiment extracted from a news article related to a financial instrument.
 */
@Value
@Builder
public class NewsSentiment {
    Long newsId;
    String symbol;
    String headline;
    String source;
    double sentimentScore; // Range: -1.0 (very negative) to +1.0 (very positive)
    LocalDateTime publishedAt;
    SentimentType sentimentType;

    public enum SentimentType {
        POSITIVE, NEGATIVE, NEUTRAL
    }

    public boolean isPositive() {
        return sentimentScore > 0.3;
    }

    public boolean isNegative() {
        return sentimentScore < -0.3;
    }

    public boolean isNeutral() {
        return !isPositive() && !isNegative();
    }
}

