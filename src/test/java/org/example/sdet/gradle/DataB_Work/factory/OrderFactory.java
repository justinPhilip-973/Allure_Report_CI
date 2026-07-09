package org.example.sdet.gradle.DataB_Work.factory;

import org.example.sdet.gradle.DataB_Work.builder.OrderBuilder;
import org.example.sdet.gradle.DataB_Work.repo.OrderRepository;

public class OrderFactory {
    private final OrderRepository repository;

    public OrderFactory(OrderRepository repository) {
        this.repository = repository;
    }

    public long persisted(OrderBuilder builder) {
        return repository.save(builder.build());
    }
}