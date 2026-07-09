package org.example.sdet.gradle.DataB_Work.model;

import java.time.LocalDate;

public record Order(
        String sku,
        int quantity,
        long totalPaise,
        String status,
        LocalDate orderedOn,
        boolean refunded
) {
}