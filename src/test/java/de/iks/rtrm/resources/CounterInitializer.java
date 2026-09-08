package de.iks.rtrm.resources;

import de.iks.rtrm.ResourceInitializer;

import java.util.HashMap;
import java.util.Map;

public class CounterInitializer implements ResourceInitializer {
    public static final Map<Class<?>, Integer> initCounts = new HashMap<>();

    @Override
    public void initialize(Class<?> resource) {
        initCounts.merge(resource, 1, Integer::sum);
    }
}
