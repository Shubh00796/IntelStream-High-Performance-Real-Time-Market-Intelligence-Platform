package com.IntelStream.domain.repository;

import com.IntelStream.domain.model.Instrument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository {

    Instrument save(Instrument instrument);

    Optional<Instrument> findById(Long id);

    Optional<Instrument> findBySymbol(String symbol);

    Page<Instrument> findByExchangeId(Long exchangeId, Pageable pageable);

    List<Instrument> findByInstrumentType(String instrumentType);

    List<Instrument> findActiveInstruments();

    Page<Instrument> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Instrument> findByExchangeIdAndInstrumentType(Long exchangeId, String instrumentType);

    void deactivateInstrument(Long id);
}