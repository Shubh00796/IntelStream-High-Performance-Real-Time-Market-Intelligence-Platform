package com.IntelStream.infrastructure.persistence.repository.impl;


import com.IntelStream.domain.model.Exchange;
import com.IntelStream.domain.repository.ExchangeRepository;
import com.IntelStream.infrastructure.persistence.entity.ExchangeEntity;
import com.IntelStream.infrastructure.persistence.mapper.ExchangeMapper;
import com.IntelStream.infrastructure.persistence.repository.ExchangeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ExchangeRepositoryImpl implements ExchangeRepository {

    private final ExchangeJpaRepository jpaRepository;
    private final ExchangeMapper mapper;

    @Override
    @Transactional
    public Exchange save(Exchange exchange) {
        log.debug("Saving exchange with code: {}", exchange.getCode());
        ExchangeEntity saved = jpaRepository.save(mapper.toEntity(exchange));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Exchange> findById(Long id) {
        return findAndMap(() -> jpaRepository.findById(id));
    }

    @Override
    public Optional<Exchange> findByCode(String code) {
        return findAndMap(() -> jpaRepository.findByCode(code));
    }

    @Override
    public List<Exchange> findAllActive() {
        return streamAndMap(jpaRepository::findAllByActiveTrue);
    }

    @Override
    public List<Exchange> findByCurrency(String currency) {
        return streamAndMap(() -> jpaRepository.findByCurrency(currency));
    }

    @Override
    public List<Exchange> findByTimezone(String timezone) {
        return streamAndMap(() -> jpaRepository.findByTimezone(timezone));
    }

    @Override
    public List<Exchange> findOpenMarketsAt(LocalDateTime currentTime) {
        return streamAndMap(() -> jpaRepository.findOpenMarketsAt(currentTime));
    }

    @Override
    @Transactional
    public void deactivateExchange(Long id) {
        log.info("Deactivating exchange with id: {}", id);
        jpaRepository.deactivateExchange(id);
    }

    // ======= 🔒 PRIVATE MAPPING HELPERS =======

    private Exchange mapToDomain(ExchangeEntity entity) {
        return mapper.toDomain(entity);
    }

    private <T> Optional<Exchange> findAndMap(Supplier<Optional<T>> supplier) {
        return supplier.get().map(obj -> mapper.toDomain((ExchangeEntity) obj));
    }

    private <T> List<Exchange> streamAndMap(Supplier<List<T>> supplier) {
        return supplier.get().stream()
                .map(obj -> mapper.toDomain((ExchangeEntity) obj))
                .collect(Collectors.toList());
    }
}
