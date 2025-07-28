package com.IntelStream.presentation.inbound.controllers.command;

import com.IntelStream.application.service.MarketDataFilterService;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketdata/filter")
@RequiredArgsConstructor
public class MarketDataFilterController {

    private final MarketDataFilterService filterService;
    private final MarketDataMapper mapper;

    @PostMapping
    public ResponseEntity<List<MarketDataResponse>> filterData(
            @RequestParam String strategy,
            @RequestBody List<MarketDataResponse> dataDtoList) {

        // Validation and transformation delegated appropriately
        List<MarketData> domainList = dataDtoList.stream()
                .map(dto -> mapper.toDomainEntity(dto)) // ensure you add this method to mapper
                .toList();

        List<MarketData> filtered = filterService.applyFilter(strategy, domainList);

        List<MarketDataResponse> responseList = filtered.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(responseList);
    }
}
