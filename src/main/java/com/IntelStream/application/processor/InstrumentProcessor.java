package com.IntelStream.application.processor;

import com.IntelStream.domain.model.Instrument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    //5. Filter By Selector
    public List<Instrument> filterBySelector(String selector) {
        return instruments
                .stream()
                .filter(instrument -> instrument.getSector().equalsIgnoreCase(selector))
                .toList();
    }

    //6. Filter by ExchangeId And Symbol
    public List<Instrument> filterByExchangeIdAndSymbol(Long exchangeId, String symbol) {
        return instruments
                .stream()
                .filter(instrument -> instrument.getExchangeId().equals(exchangeId) && instrument.getSymbol().equalsIgnoreCase(symbol))
                .toList();
    }

    //7. Filter by ExchnageID and Currency
    public List<Instrument> filterByExchangeIdandCurrency(Long exchangeId, String currency) {
        return instruments
                .stream()
                .filter(instrument -> instrument.getExchangeId().equals(exchangeId) && instrument.getCurrency().equalsIgnoreCase(currency))
                .toList();
    }

    //8. Gropup by InstrumentType
    public Map<String, List<Instrument>> groupByInstrumentType() {
        return instruments
                .stream()
                .collect(Collectors.groupingBy(Instrument::getInstrumentType));
    }

    //9. Group by Sector
    public Map<String, List<Instrument>> groupBySector() {
        return instruments
                .stream()
                .collect(Collectors.groupingBy(instrument -> instrument.getSector()));
    }

    //10. Group by ExchangeId and Currency
    public Map<String, List<Instrument>> groupByExchangeIdAndCurrency() {
        return instruments
                .stream()
                .collect(Collectors.groupingBy(instrument -> instrument.getExchangeId() + "-" + instrument.getCurrency()));
    }

    //11. Group by ExchangeId and Active Status
    public Map<String, List<Instrument>> groupByExchangeIdAndActiveStatus() {
        return instruments
                .stream()
                .collect(Collectors.groupingBy(instrument -> instrument.getExchangeId() + "_" + instrument.isActive()));
    }

    //12. Map InstrumentId to Symbol
    public Map<Long, String> mapInstrumentIdToSymbol() {
        return instruments
                .stream()
                .collect(Collectors.toMap(Instrument::getId, Instrument::getSymbol));
    }

    //13 Map Symbol to ExchangeId
    public Map<String, Long> mapSymbolToExchangeId() {
        return instruments
                .stream()
                .collect(Collectors.toMap(Instrument::getSymbol, Instrument::getExchangeId));
    }

    //14. Get All Unique Sectors
    public Set<String> getAllUniqueSectors() {
        return instruments
                .stream()
                .map(Instrument::getSector)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    //15. Get All Unique Isntrument names
    public Set<String> getAllUniqueInstrumentsNames() {
        return instruments
                .stream()
                .map(Instrument::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    //16. Map Instrument Symbol To ActiveStatus
    public Map<String, Boolean> mapInstrumentSymbolToActiveStatus() {
        return instruments
                .stream()
                .collect(Collectors.toMap(Instrument::getSymbol, Instrument::isActive));
    }

    //17. Count Active Instruments
    public long countActiveInstruments() {
        return instruments.stream()
                .filter(Instrument::isActive)
                .count();
    }

    //18. Count By InstrumentType
    public long countByInstrumentType(String type) {
        return instruments
                .stream()
                .filter(instrument -> instrument.getInstrumentType().equalsIgnoreCase(type))
                .count();
    }

    //19. Count By Sector
    public long countBySector(String sector) {
        return instruments
                .stream()
                .filter(instrument -> instrument.getSector().equalsIgnoreCase(sector))
                .count();
    }

    //20. Count By Currency
    public long countByCurrency(String currency) {
        return instruments
                .stream()
                .filter(instrument -> instrument.getCurrency().equalsIgnoreCase(currency))
                .count();
    }

}
