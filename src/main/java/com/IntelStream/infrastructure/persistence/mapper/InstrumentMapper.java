package com.IntelStream.infrastructure.persistence.mapper;


import com.IntelStream.domain.model.Instrument;
import com.IntelStream.infrastructure.persistence.entity.InstrumentEntity;
import com.IntelStream.presentation.dto.response.InstrumentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {
    Instrument toDomain(InstrumentEntity instrumentEntity);
    InstrumentEntity toEntity(Instrument instrument);

        InstrumentResponse toResponse(Instrument instrument);

}
