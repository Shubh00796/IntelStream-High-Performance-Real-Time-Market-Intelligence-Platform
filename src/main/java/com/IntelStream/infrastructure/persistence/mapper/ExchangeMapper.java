package com.IntelStream.infrastructure.persistence.mapper;


import com.IntelStream.domain.model.Exchange;
import com.IntelStream.infrastructure.persistence.entity.ExchangeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeMapper {
    Exchange toDomain(ExchangeEntity exchangeEntity);
    ExchangeEntity toEntity(Exchange exchange);
}
