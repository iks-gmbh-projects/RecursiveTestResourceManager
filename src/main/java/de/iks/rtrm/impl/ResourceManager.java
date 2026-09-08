package de.iks.rtrm.impl;

import de.iks.rtrm.InitializeWith;
import de.iks.rtrm.UsesResource;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ExtensionContext.StoreScope;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLocksProvider;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main JUnit 6 extension
 */
public class ResourceManager implements BeforeEachCallback, AfterEachCallback {
    private static final Namespace NAMESPACE = Namespace.create(ResourceManager.class);

    static Map<Class<?>, Boolean> getDeclaredResourceUsage(
        List<Class<?>> enclosingInstanceTypes,
        Class<?> testClass,
        Method testMethod
    ) {
        return Stream
            .concat(
                enclosingInstanceTypes.stream(),
                Stream.of(testClass, testMethod)
            )
            .map(e -> AnnotationSupport.findRepeatableAnnotations(e, UsesResource.class))
            .flatMap(List::stream)
            .collect(Collectors.toUnmodifiableMap(
                UsesResource::value,
                UsesResource::dirties,
                (a, b) -> b
            ));
    }

    private static synchronized void initialize(Class<?> resource, Set<Class<?>> initialized, Store store) throws Exception {
        if(initialized.contains(resource)) return;

        for(Class<?> requiredResource : resource.getInterfaces()) {
            initialize(requiredResource, initialized, store);
        }

        for(InitializeWith initializer : AnnotationSupport.findRepeatableAnnotations(resource, InitializeWith.class)) {
            store.computeIfAbsent(initializer.value())
                .initialize(resource);
        }

        initialized.add(resource);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        final var initialized = context
            .getStore(StoreScope.EXECUTION_REQUEST, NAMESPACE)
            .computeIfAbsent(ResourceManagerState.class)
            .initialized();
        getDeclaredResourceUsage(
            context.getEnclosingTestClasses(),
            context.getRequiredTestClass(),
            context.getRequiredTestMethod()
        ).forEach((resource, dirties) -> {
            if(dirties) synchronized(ResourceManager.class) {
                initialized.removeIf(resource::isAssignableFrom);
            }
        });
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final Store store = context.getStore(StoreScope.EXECUTION_REQUEST, NAMESPACE);
        final var initialized = store
            .computeIfAbsent(ResourceManagerState.class)
            .initialized();
        for(Class<?> resource : getDeclaredResourceUsage(
            context.getEnclosingTestClasses(),
            context.getRequiredTestClass(),
            context.getRequiredTestMethod()
        ).keySet()) {
            initialize(resource, initialized, store);
        }
    }

    /**
     * The {@link ResourceLocksProvider} that ensures managed resources are properly accessed when testing in parallel.
     */
    public static class Locks implements ResourceLocksProvider {
        @Override
        @NullMarked
        public Set<Lock> provideForMethod(
            List<Class<?>> enclosingInstanceTypes,
            Class<?> testClass,
            Method testMethod
        ) {
            return getDeclaredResourceUsage(enclosingInstanceTypes, testClass, testMethod)
                .entrySet()
                .stream()
                .map(e -> new Lock(
                    e
                        .getKey()
                        .getCanonicalName(),
                    e.getValue() ? ResourceAccessMode.READ_WRITE : ResourceAccessMode.READ
                ))
                .collect(Collectors.toUnmodifiableSet());
        }

    }
}
