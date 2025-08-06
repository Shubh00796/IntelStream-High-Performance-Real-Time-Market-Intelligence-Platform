package com.IntelStream.infrastructure.persistence.mapper;


import com.IntelStream.infrastructure.persistence.entity.LoanApplication;
import com.IntelStream.presentation.dto.response.LoanApplicationResponse;
import com.IntelStream.presentation.dto.request.LoanRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    // Request DTO -> Domain Entity
    LoanApplication toEntity(LoanRequest request);

    // Domain Entity -> Request DTO (if needed)
    LoanRequest toDto(LoanApplication entity);

    // Domain Entity -> Response DTO
    LoanApplicationResponse toResponse(LoanApplication application);
}
