package com.IntelStream.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_snapshots",
        indexes = {
                @Index(name = "idx_analytics_instrument_calculated", columnList = "instrument_id, calculated_at"),
                @Index(name = "idx_analytics_calculated_at", columnList = "calculated_at")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"createdAt", "updatedAt"})
@EqualsAndHashCode(of = "id")
public class AnalyticsSnapshotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instrument_id", nullable = false)
    private Long instrumentId;

    @Column(name = "moving_average_20", precision = 18, scale = 8)
    private BigDecimal movingAverage20;

    @Column(name = "moving_average_50", precision = 18, scale = 8)
    private BigDecimal movingAverage50;

    @Column(precision = 18, scale = 8)
    private BigDecimal volatility;

    @Column(precision = 5, scale = 2)
    private BigDecimal rsi;

    @Column(name = "volume_24h", precision = 18, scale = 8)
    private BigDecimal volume24h;

    @Column(name = "price_change_24h", precision = 18, scale = 8)
    private BigDecimal priceChange24h;

    @Column(name = "price_change_percent_24h", precision = 8, scale = 4)
    private BigDecimal priceChangePercent24h;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
