package com.IntelStream.application.command.service;

import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.domain.model.Exchange;
import com.IntelStream.domain.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeCommandService {

    private final ExchangeRepository exchangeRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @CacheEvict(cacheNames = {"instruments"}, allEntries = true)
    public Long createExchange(Exchange exchange) {
        Exchange saved = exchangeRepository.save(exchange);
        log.info("Exchange created with ID: {}", saved.getId());
        return saved.getId();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @CacheEvict(cacheNames = {"instruments"}, allEntries = true)
    public void deactivateExchange(Long exchangeId) {
        exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange not found with ID: " + exchangeId));

        exchangeRepository.deactivateExchange(exchangeId);
        log.info("Exchange deactivated with ID: {}", exchangeId);
    }
}
