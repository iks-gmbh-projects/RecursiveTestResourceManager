package de.iks.rtrm;

/**
 * <p>A hook to initialize a managed resource.</p>
 * <p>A ResourceInitializer must only initialize the requested resource, and not any resources the requested resource depends on.</p>
 * <p>It is guaranteed that by the time initialization of the given resource is requested, all resources it depends on have already been initialized.</p>
 * <p>No guarantee is made about the order of execution between initializers that can initialize the given resource.</p>
 * <p>A ResourceInitializer is also called to reinitialize a resource after it, or a resource it depends on, was dirtied by a test. This has the same ordering semantics as fresh initialization.</p>
 * <p>A ResourceInitializer must do nothing and return if it does not know how to initialize the given resource.</p>
 * <p>If no ResourceInitializer can initialize a given resource, it is assumed to be a meta-resource, for which initialization is a no-op.</p>
 */
public interface ResourceInitializer {
    /**
     * Requests initialization of a resource.
     *
     * @param resource <p>the resource to initialize</p>
     * @throws Exception <p>If an error occurred during initialization.</p>
     */
    void initialize(Class<?> resource) throws Exception;
}
