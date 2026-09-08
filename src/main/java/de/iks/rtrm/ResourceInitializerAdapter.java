package de.iks.rtrm;

import java.util.Objects;

/**
 * Partial ResourceInitializer implementation for initialization of just a single resource.
 */
public abstract class ResourceInitializerAdapter implements ResourceInitializer {
    private final Class<?> resource;

    /**
     * @param resource the class instance of the interface describing the resource this ResourceInitializer can initialize.
     */
    public ResourceInitializerAdapter(Class<?> resource) {
        this.resource = resource;
    }

    @Override
    public void initialize(Class<?> resource) throws Exception {
        if(Objects.equals(resource, this.resource)) {
            initialize();
        }
    }

    /**
     * Called by {@link #initialize(Class)} when the requested resource is the one this ResourceInitializer can initialize.
     * @throws Exception If an Error occurs during (re-)initialization of the resource.
     */
    public abstract void initialize() throws Exception;
}
