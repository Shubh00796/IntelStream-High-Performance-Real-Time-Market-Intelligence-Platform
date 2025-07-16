package com.IntelStream.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "instruments",
        indexes = {
                @Index(name = "idx_instruments_symbol", columnList = "symbol", unique = true),
                @Index(name = "idx_instruments_exchange_type", columnList = "exchange_id, instrument_type"),
                @Index(name = "idx_instruments_active", columnList = "active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"createdAt", "updatedAt"})
@EqualsAndHashCode(of = "id")
public class InstrumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String symbol;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(name = "instrument_type", length = 20, nullable = false)
    private String instrumentType;

    @Column(length = 100)
    private String sector;

    @Column(name = "exchange_id", nullable = false)
    private Long exchangeId;

    @Column(length = 3, nullable = false)
    private String currency;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (active == null) active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

