# IntelStream-High-Performance-Real-Time-Market-Intelligence-Platform
 We'll build a Real-Time Market Intelligence Platform - a sophisticated system that aggregates, processes, and serves financial market data for trading firms, investment banks, and fintech companies.


<!-- README.md -->

<h3>🧠 IntelStream Architecture Diagram</h3>

<div style="height: 1800px; overflow: auto; border: 1px solid #ccc; padding: 10px; background-color: #1e1e1e; color: white;">
  
```mermaid
flowchart TD
    %% CORE LAYERS
    subgraph Domain Layer
        D1[MarketData]
        D2[Instrument]
        D3[AnalyticsSnapshot]
        D4[Exchange]
        D5[PriceMovement]
        DS1[MarketAnalyticsService]
        DS2[PriceCalculationService]
        DS3[RiskAssessmentService]
        DE1[MarketDataUpdatedEvent]
        DE2[PriceAlertTriggeredEvent]
    end

    subgraph Application Layer
        subgraph Command
            CHandler1[CreateInstrumentCommandHandler]
            CHandler2[UpdateMarketDataCommandHandler]
            CHandler3[GenerateAnalyticsCommandHandler]
            CService1[InstrumentCommandService]
            CService2[MarketDataCommandService]
        end
        subgraph Query
            QHandler1[GetMarketDataQueryHandler]
            QHandler2[GetAnalyticsQueryHandler]
            QHandler3[GetInstrumentListQueryHandler]
            QService1[MarketDataQueryService]
            QService2[AnalyticsQueryService]
        end
        ACommon[Exception, Validator, DTO Mapper]
    end

    subgraph Ports
        subgraph Inbound Ports
            PortIn1[MarketDataUseCase]
            PortIn2[AnalyticsUseCase]
        end
        subgraph Outbound Ports
            PortOut1[MarketDataRepository]
            PortOut2[ExternalDataProvider]
            PortOut3[EventPublisher]
        end
    end

    subgraph Infrastructure Layer
        subgraph Config
            C1[DatabaseConfig]
            C2[CacheConfig]
            C3[AsyncConfig]
            C4[SecurityConfig]
        end
        subgraph Persistence
            P1[MarketDataEntity]
            P2[InstrumentEntity]
            P3[AnalyticsSnapshotEntity]
            P4[ExchangeEntity]
            R1[MarketDataRepositoryImpl]
            R2[InstrumentRepositoryImpl]
            R3[AnalyticsRepositoryImpl]
        end
        subgraph External
            E1[MarketDataFeedAdapter]
            E2[ExchangeApiClient]
            E3[AlertServiceAdapter]
        end
        subgraph Messaging
            Pub[MarketEventPublisher]
            Con[MarketDataConsumer]
        end
    end

    subgraph Presentation Layer
        Ctrl1[MarketDataController]
        Ctrl2[AnalyticsController]
        Ctrl3[DashboardController]
        WS[RealTimeMarketDataController]
        APIMapper[API DTO Mappers]
    end

    subgraph Shared Layer
        S1[Utils: Pageable, DateTime, Validation]
        S2[Constants: CacheNames, MarketConstants]
        S3[Generic: BaseEntity, GenericRepo, GenericService]
    end

    %% FLOW CONNECTIONS

    %% Presentation -> Application
    Ctrl1 --> PortIn1
    Ctrl2 --> PortIn2
    WS --> PortIn1

    %% Application <-> Domain
    CHandler1 --> CService1 --> D2
    CHandler2 --> CService2 --> D1
    CHandler3 --> DS1 --> D3

    QHandler1 --> QService1 --> D1
    QHandler2 --> QService2 --> D3
    QHandler3 --> QService1 --> D2

    %% Application -> Ports
    CService1 --> PortOut1
    CService2 --> PortOut1
    QService1 --> PortOut1
    QService2 --> PortOut1
    DS1 --> PortOut3
    DS2 --> PortOut2

    %% Ports -> Infra
    PortOut1 --> R1
    PortOut1 --> R2
    PortOut1 --> R3
    PortOut2 --> E1
    PortOut2 --> E2
    PortOut3 --> Pub
    Con --> CHandler2

    %% Domain Events
    D1 --> DE1
    D5 --> DE2

    %% Kafka Flow
    Pub -->|Kafka| Broker1[(Broker)]
    Broker1 -->|Topic| Con

    %% Mappers
    P1 --> R1
    P2 --> R2
    P3 --> R3

    %% Shared Layer Usage
    ACommon --> CService1
    ACommon --> QService2
    S1 --> everything
    S2 --> everything
    S3 --> everything
