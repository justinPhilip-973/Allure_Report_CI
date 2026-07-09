package org.example.sdet.gradle.DataB_Work.data;

import org.example.sdet.gradle.DataB_Work.factory.OrderFactory;
import org.example.sdet.gradle.DataB_Work.repo.OrderRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.example.sdet.gradle.DataB_Work.builder.OrderBuilder.anOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@Tag("integration")
public class OrderData {

    @Container
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4")
                    .withDatabaseName("retail_test")
                    .withUsername("user")
                    .withPassword("password");

    static OrderRepository repository;
    static OrderFactory factory;

    @BeforeAll
    static void migrateSchema() {

        Flyway.configure()
                .dataSource(
                        mysql.getJdbcUrl(),
                        mysql.getUsername(),
                        mysql.getPassword()
                )
                .locations("classpath:db/migration")
                .load()
                .migrate();

        repository = new OrderRepository(
                mysql.getJdbcUrl(),
                mysql.getUsername(),
                mysql.getPassword()
        );

        factory = new OrderFactory(repository);
    }

    @BeforeEach
    void resetMutableTables() {
        repository.resetMutableTables();
    }

    @Test
    void flywaySeedsReferenceDataButNoPerTestOrders() {

        assertEquals(
                4,
                repository.referenceStatusCount()
        );

        assertEquals(
                0,
                repository.count()
        );
    }

    @Test
    void factoryPersistsBuilderDataAgainstIsolatedMySql() {

        long id = factory.persisted(
                anOrder()
                        .setQuantityAs(3)
        );

        assertTrue(id > 0);
        assertEquals(1, repository.count());
    }

    @Test
    void countsOnlyThisTestsOrders() {

        factory.persisted(anOrder());

        factory.persisted(
                anOrder()
                        .setSkuAs("SKU-RET-202")
                        .setQuantityAs(2)
        );

        assertEquals(2, repository.count());
    }

    @Test
    void resetMakesTestsOrderIndependent() {

        assertEquals(
                0,
                repository.count()
        );

        factory.persisted(
                anOrder()
                        .refunded()
        );

        assertEquals(1, repository.count());
        assertEquals(
                1,
                repository.countByStatus("REFUNDED")
        );
    }
}