package de.iks.rtrm;

import de.iks.rtrm.InitializeWith.Repeated;

import java.lang.annotation.*;

/**
 * Specifies that this class identifies a non-meta resource, and provides a {@link ResourceInitializer} implementation to initialize it with
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Repeated.class)
public @interface InitializeWith {
    /**
     * @return the ResourceInitializer implementation to initialize the resource identified by the annotated class
     */
    Class<? extends ResourceInitializer> value();

    /**
     * @hidden
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Repeated {
        InitializeWith[] value();
    }
}
