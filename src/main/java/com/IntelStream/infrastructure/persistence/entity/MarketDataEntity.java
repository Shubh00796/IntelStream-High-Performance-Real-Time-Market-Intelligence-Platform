package com.IntelStream.infrastructure.persistence.entity;


import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_data",
        indexes = {
                @Index(name = "idx_market_data_instrument_timestamp", columnList = "instrument_id, timestamp"),
                @Index(name = "idx_market_data_exchange_timestamp", columnList = "exchange_id, timestamp"),
                @Index(name = "idx_market_data_timestamp", columnList = "timestamp")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"createdAt", "updatedAt"})
@EqualsAndHashCode(of = "id")
public class MarketDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instrument_id", nullable = false)
    private Long instrumentId;

    @Column(name = "exchange_id", nullable = false)
    private Long exchangeId;

    @Column(name = "price", precision = 18, scale = 8, nullable = false)
    private BigDecimal price;

    @Column(name = "volume", precision = 18, scale = 8, nullable = false)
    private BigDecimal volume;

    @Column(name = "bid_price", precision = 18, scale = 8)
    private BigDecimal bidPrice;

    @Column(name = "ask_price", precision = 18, scale = 8)
    private BigDecimal askPrice;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "source", length = 50)
    private String source;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}