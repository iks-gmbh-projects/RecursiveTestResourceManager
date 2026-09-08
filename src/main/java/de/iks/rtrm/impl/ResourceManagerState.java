package de.iks.rtrm.impl;

import java.util.HashSet;
import java.util.Set;

/**
 * @hidden
 */
public record ResourceManagerState(Set<Class<?>> initialized) {
    public ResourceManagerState() {
        this(new HashSet<>());
    }
}
