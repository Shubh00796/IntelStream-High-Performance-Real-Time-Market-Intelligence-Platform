package com.IntelStream.infrastructure.persistence.mapper;


import com.IntelStream.domain.model.Exchange;
import com.IntelStream.domain.model.Instrument;
import com.IntelStream.infrastructure.persistence.entity.ExchangeEntity;
import com.IntelStream.presentation.dto.response.ExchangeResponse;
import com.IntelStream.presentation.dto.response.InstrumentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeMapper {
    Exchange toDomain(ExchangeEntity exchangeEntity);

    ExchangeEntity toEntity(Exchange exchange);

    ExchangeResponse toResponse(Exchange exchange);

}
