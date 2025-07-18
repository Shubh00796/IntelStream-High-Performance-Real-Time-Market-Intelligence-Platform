package com.IntelStream.infrastructure.persistence.mapper;


import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.infrastructure.persistence.entity.AnalyticsSnapshotEntity;
import com.IntelStream.presentation.dto.response.AnalyticsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalyticsSnapshotMapper {
    AnalyticsSnapshot toDomain(AnalyticsSnapshotEntity analyticsSnapshotEntity);

    AnalyticsSnapshotEntity toEntity(AnalyticsSnapshot analyticsSnapshot);

    AnalyticsResponse toResponce(AnalyticsSnapshot analyticsSnapshot);
}
