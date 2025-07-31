package com.IntelStream.application.processor;

import com.IntelStream.domain.model.Instrument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
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
}
