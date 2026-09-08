package de.iks.rtrm.test;

import de.iks.rtrm.UsesResource;
import de.iks.rtrm.resources.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourceManagerTests {
    private static void assertCounts(Map<Class<?>, Integer> expected) {
        expected.forEach((clazz, count) -> assertEquals(
            count,
            CounterInitializer.initCounts.getOrDefault(clazz, 0),
            clazz.getCanonicalName()
        ));
    }

    @Order(0)
    @Test
    public void testNoResources() {
        assertCounts(Map.of(
            A.class, 0,
            B.class, 0,
            C.class, 0,
            D.class, 0
        ));
    }

    @Order(1)
    @Test
    @UsesResource(A.class)
    public void testInitA() {
        assertCounts(Map.of(
            A.class, 1,
            B.class, 0,
            C.class, 0,
            D.class, 0
        ));
    }

    @Order(2)
    @Test
    @UsesResource(value = A.class, dirties = true)
    public void testNoReinitA() {
        assertCounts(Map.of(
            A.class, 1,
            B.class, 0,
            C.class, 0,
            D.class, 0
        ));
    }

    @Order(3)
    @Test
    @UsesResource(B.class)
    public void testInitAB() {
        assertCounts(Map.of(
            A.class, 2,
            B.class, 1,
            C.class, 0,
            D.class, 0
        ));
    }

    @Order(4)
    @Test
    @UsesResource(value = A.class, dirties = true)
    public void testNoReinitAB() {
        assertCounts(Map.of(
            A.class, 2,
            B.class, 1,
            C.class, 0,
            D.class, 0
        ));
    }

    @Order(5)
    @Test
    @UsesResource(value = D.class, dirties = true)
    public void testInitACD() {
        assertCounts(Map.of(
            A.class, 3,
            B.class, 1,
            C.class, 1,
            D.class, 1
        ));
    }

    @Order(6)
    @Test
    @UsesResource(B.class)
    @UsesResource(D.class)
    public void testNoReinitACInitD() {
        assertCounts(Map.of(
            A.class, 3,
            B.class, 2,
            C.class, 1,
            D.class, 2
        ));
    }
}
