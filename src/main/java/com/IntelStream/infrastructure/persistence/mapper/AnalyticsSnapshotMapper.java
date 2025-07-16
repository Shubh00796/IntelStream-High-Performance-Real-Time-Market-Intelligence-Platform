package com.IntelStream.infrastructure.persistence.mapper;


import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.infrastructure.persistence.entity.AnalyticsSnapshotEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalyticsSnapshotMapper {
    AnalyticsSnapshot toDomain(AnalyticsSnapshotEntity analyticsSnapshotEntity);
    AnalyticsSnapshotEntity toEntity(AnalyticsSnapshot analyticsSnapshot);
}
