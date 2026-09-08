package de.iks.rtrm;

import de.iks.rtrm.UsesResource.Repeated;
import de.iks.rtrm.impl.ResourceManager;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * <p>Marks that a given test requires a resource to have been initialized.</p>
 * <p>Class-Level annotations are equivalent to placing the annotation on all declared methods and inner classes.</p>
 * <hr>
 * <p>A resource is uniquely identified by an interface.</p>
 * <p>Dependencies are modeled by inheritance: If resource A requires resource B, then the interface identifying A extends the interface identifying B</p>
 * <p>If a resource does not depend on any other resources, the interface identifying it extends no other interfaces.</p>
 */
@Target({METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Repeated.class)
@Inherited
@ResourceLock(providers = ResourceManager.Locks.class)
public @interface UsesResource {
    /**
     * @return the required resource
     */
    Class<?> value();

    /**
     * @return whether this test alters the resource in such a way that it requires reinitialization before being used in other tests
     */
    boolean dirties() default false;

    /**
     * @hidden
     */
    @Target({METHOD, TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface Repeated {
        UsesResource[] value() default {};
    }
}
