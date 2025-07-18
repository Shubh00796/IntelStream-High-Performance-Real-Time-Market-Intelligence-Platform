package com.IntelStream.application.query.service.instrument;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class QuerySelector<T> {
    private final List<ConditionSupplier<T>> conditions = new ArrayList<>();

    public QuerySelector<T> when(Predicate<Void> condition, Supplier<T> supplier) {
        conditions.add(new ConditionSupplier<>(condition, supplier));
        return this;
    }

    public T orElse(Supplier<T> defaultSupplier) {
        return conditions.stream()
                .filter(c -> c.condition.test(null))
                .findFirst()
                .map(c -> c.supplier.get())
                .orElseGet(defaultSupplier);
    }

    private static class ConditionSupplier<T> {
        Predicate<Void> condition;
        Supplier<T> supplier;

        ConditionSupplier(Predicate<Void> condition, Supplier<T> supplier) {
            this.condition = condition;
            this.supplier = supplier;
        }
    }
}
