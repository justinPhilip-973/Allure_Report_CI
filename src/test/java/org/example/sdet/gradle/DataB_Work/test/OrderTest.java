package org.example.sdet.gradle.DataB_Work.test;

import org.example.sdet.gradle.DataB_Work.builder.OrderBuilder;
import org.example.sdet.gradle.DataB_Work.model.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.example.sdet.gradle.DataB_Work.builder.OrderBuilder.anOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void builderUsesSensibleDefaults() {

        Order order = anOrder().build();

        assertEquals("SKU-RET-101", order.sku());
        assertEquals(1, order.quantity());
        assertEquals(129_900, order.totalPaise());
        assertEquals("NEW", order.status());
        assertEquals(LocalDate.of(2026, 7, 8), order.orderedOn());
        assertEquals(false, order.refunded());
    }

    @Test
    void builderStatesOnlyWhatTheTestCaresAbout() {

        Order order = anOrder()
                .setQuantityAs(3)
                .build();
        assertEquals(3, order.quantity());
        assertEquals("SKU-RET-101", order.sku(),
                "Unrelated fields come from defaults"
        );

        assertEquals("NEW", order.status(),
                "Unrelated fields come from defaults"
        );
    }

    @Test
    void builderSupportsNamedBusinessStatesWithoutFixtureDuplication() {

        Order order = anOrder()
                .refunded()
                .build();

        assertEquals("REFUNDED", order.status());
        assertEquals(true, order.refunded());
    }

    @Test
    void invalidBuilderDataFailsBeforeDatabaseSetup() {
        OrderBuilder builder =
                anOrder()
                        .setQuantityAs(0);
        assertThrows(
                IllegalArgumentException.class,
                builder::build
        );
    }
}