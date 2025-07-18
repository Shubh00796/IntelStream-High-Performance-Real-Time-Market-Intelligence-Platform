package com.IntelStream.application.query.service.instrument;

import com.IntelStream.domain.model.Instrument;

import java.util.function.Predicate;

public interface InstrumentFilter extends Predicate<Instrument> {
    static InstrumentFilter bySector(String sector) {
        return instrument -> sector == null || sector.isBlank() || sector.equalsIgnoreCase(instrument.getSector());
    }
    static InstrumentFilter byActiveOnly(boolean activeOnly){
        return instrument -> !activeOnly || instrument.isActive();
    }
    private static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }
}
