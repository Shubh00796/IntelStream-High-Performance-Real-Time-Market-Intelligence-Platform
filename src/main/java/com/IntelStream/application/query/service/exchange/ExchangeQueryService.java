package com.IntelStream.application.query.service.exchange;


import com.IntelStream.domain.model.Exchange;
import com.IntelStream.domain.repository.ExchangeRepository;
import com.IntelStream.infrastructure.persistence.mapper.ExchangeMapper;
import com.IntelStream.presentation.dto.response.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeQueryService {

    private final ExchangeRepository exchangeRepository;
    private final ExchangeMapper exchangeMapper;

    @Cacheable(cacheNames = "instruments", key = "'exchange:' + #id")
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public ExchangeResponse getById(Long id) {
        Exchange exchange = exchangeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exchange not found with ID: " + id));
        return exchangeMapper.toResponse(exchange);
    }

    @Cacheable(cacheNames = "instruments", key = "'exchange:code:' + #code")
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public ExchangeResponse getByCode(String code) {
        Exchange exchange = exchangeRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Exchange not found with code: " + code));
        return exchangeMapper.toResponse(exchange);
    }

    public List<ExchangeResponse> getAllActive() {
        return exchangeRepository.findAllActive().stream()
                .map(exchangeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ExchangeResponse> getByCurrency(String currency) {
        return exchangeRepository.findByCurrency(currency).stream()
                .map(exchangeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ExchangeResponse> getByTimezone(String timezone) {
        return exchangeRepository.findByTimezone(timezone).stream()
                .map(exchangeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ExchangeResponse> getOpenMarketsAt(LocalDateTime currentTime) {
        return exchangeRepository.findOpenMarketsAt(currentTime).stream()
                .map(exchangeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public boolean isMarketOpen(Long exchangeId, LocalDateTime currentTime) {
        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new IllegalArgumentException("Exchange not found with ID: " + exchangeId));
        return exchange.isMarketOpen(currentTime); // ✅ Domain logic used here
    }
}
