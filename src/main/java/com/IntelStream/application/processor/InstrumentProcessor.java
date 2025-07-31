package com.IntelStream.application.processor;

import com.IntelStream.domain.model.Instrument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InstrumentProcessor {

    private final List<Instrument> instruments;

    //1. Filter Active Instruments
    public List<Instrument> filterActiveInstruments() {
        return instruments
                .stream()
                .filter(Instrument::isActive)
                .toList();
    }

    //2. Get Instruments by Type
    public List<Instrument> getInstrumentByType(String type) {
        return instruments
                .stream()
                .filter(instrument -> instrument.isType(type))
                .toList();
    }

    //3. Filter Inactive Instruments
    public List<Instrument> filterInactiveInstrumenst() {
        return instruments
                .stream()
                .filter(instrument -> !instrument.isActive())
                .toList();
    }

    //4. Get Instruments by Name
    public List<Instrument> getIsntrumentsByName(String name) {
        return instruments
                .stream()
                .filter(instrument -> instrument.getName().equalsIgnoreCase(name))
                .collect(Collectors.toUnmodifiableList());
    }

}
