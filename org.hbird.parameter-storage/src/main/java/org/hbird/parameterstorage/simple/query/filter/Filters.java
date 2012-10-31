package org.hbird.parameterstorage.simple.query.filter;

import org.hbird.parameterstorage.api.query.filter.Filter;

public class Filters {

    public static <T> Filter<T> acceptAll() {
        return new AcceptAllFilter<T>();
    }

    public static <T> Filter<T> and(Filter<T>... filters) {
        return new AndFilter<T>(filters);
    }

    public static <T> Filter<T> before(Long timestamp) {
        return new BeforeFilter<T>(timestamp);
    }

    public static <T> Filter<T> stopAfter(Long timestamp) {
        return new StopAfterFilter<T>(timestamp);
    }
}
