package com.IntelStream.infrastructure.persistence.mapper;


import com.IntelStream.application.query.dto.TopMoversQuery;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.infrastructure.persistence.entity.MarketDataEntity;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface MarketDataMapper {
    MarketData toDomain(MarketDataEntity marketDataEntity);

    MarketDataEntity toEntity(MarketData marketData);

    @Mappings({
            @Mapping(target = "spread", source = ".", qualifiedByName = "calculateSpread")
    })
    MarketDataResponse toResponse(MarketData marketData);

    TopMoversQuery moversToResponce(MarketData marketData);


    @Named("calculateSpread")
    default BigDecimal calculateSpread(MarketData data) {
        if (data.getAskPrice() != null && data.getBidPrice() != null) {
            return data.getAskPrice().subtract(data.getBidPrice());
        }
        return BigDecimal.ZERO;
    }


    MarketData toDomainEntity(MarketDataResponse responseDto);
}
