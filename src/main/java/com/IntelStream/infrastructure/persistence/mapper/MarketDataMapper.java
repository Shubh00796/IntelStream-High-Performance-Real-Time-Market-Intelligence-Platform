package com.IntelStream.infrastructure.persistence.mapper;


import com.IntelStream.domain.model.MarketData;
import com.IntelStream.infrastructure.persistence.entity.MarketDataEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarketDataMapper {
    MarketData toDomain(MarketDataEntity marketDataEntity);

    MarketDataEntity toEntity(MarketData marketData);
}
