package com.IntelStream.domain.repository;


import com.IntelStream.domain.model.Exchange;

import java.util.List;
import java.util.Optional;

public interface ExchangeRepository {

    Exchange save(Exchange exchange);

    Optional<Exchange> findById(Long id);

    Optional<Exchange> findByCode(String code);

    List<Exchange> findAllActive();

    List<Exchange> findByCurrency(String currency);

    List<Exchange> findByTimezone(String timezone);

    List<Exchange> findOpenMarketsAt(java.time.LocalDateTime currentTime);

    void deactivateExchange(Long id);
}
