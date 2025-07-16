package com.IntelStream.infrastructure.persistence.repository.impl;


import com.IntelStream.domain.model.Instrument;
import com.IntelStream.domain.repository.InstrumentRepository;
import com.IntelStream.infrastructure.persistence.entity.InstrumentEntity;
import com.IntelStream.infrastructure.persistence.mapper.InstrumentMapper;
import com.IntelStream.infrastructure.persistence.repository.InstrumentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InstrumentRepositoryImpl implements InstrumentRepository {

    private final InstrumentJpaRepository jpaRepository;
    private final InstrumentMapper mapper;

    @Override
    @Transactional
    public Instrument save(Instrument instrument) {
        log.debug("Saving instrument with symbol: {}", instrument.getSymbol());
        InstrumentEntity saved = jpaRepository.save(mapper.toEntity(instrument));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Instrument> findById(Long id) {
        return findAndMap(() -> jpaRepository.findById(id));
    }

    @Override
    public Optional<Instrument> findBySymbol(String symbol) {
        return findAndMap(() -> jpaRepository.findBySymbol(symbol));
    }

    @Override
    public Page<Instrument> findByExchangeId(Long exchangeId, Pageable pageable) {
        return jpaRepository.findByExchangeId(exchangeId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<Instrument> findByInstrumentType(String instrumentType) {
        return streamAndMap(() -> jpaRepository.findByInstrumentTypeAndActiveTrue(instrumentType));
    }

    @Override
    public List<Instrument> findActiveInstruments() {
        return streamAndMap(jpaRepository::findActiveInstruments);
    }

    @Override
    public Page<Instrument> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return jpaRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<Instrument> findByExchangeIdAndInstrumentType(Long exchangeId, String instrumentType) {
        return streamAndMap(() ->
                jpaRepository.findByExchangeIdAndInstrumentTypeAndActiveTrue(exchangeId, instrumentType));
    }

    @Override
    @Transactional
    public void deactivateInstrument(Long id) {
        log.info("Deactivating instrument with ID: {}", id);
        jpaRepository.deactivateInstrument(id);
    }

    // ======= 🔒 PRIVATE MAPPING HELPERS =======

    private <T> Optional<Instrument> findAndMap(Supplier<Optional<T>> supplier) {
        return supplier.get().map(obj -> mapper.toDomain((InstrumentEntity) obj));
    }

    private <T> List<Instrument> streamAndMap(Supplier<List<T>> supplier) {
        return supplier.get().stream()
                .map(obj -> mapper.toDomain((InstrumentEntity) obj))
                .collect(Collectors.toList());
    }
}
