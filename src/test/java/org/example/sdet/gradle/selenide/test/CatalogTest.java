package org.example.sdet.gradle.selenide.test;

import org.example.sdet.gradle.selenide.pages.Catalog;
import org.example.sdet.gradle.selenide.support.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatalogTest {
    @BeforeAll
    static void setup(){
        TestConfig.apply();
    }
    @Test
    @DisplayName("Exercise 1: Search for product using Selenide & Build a selenide page object")
    void verifyA_SimpleSearchForProduct(){
        Catalog catalog  =new Catalog();
        catalog.openCatalogPage();
//        catalog.;
    }

    @Test
    @DisplayName("Exercise 2: Search for product using Selenide & Build a selenide page object")
    void verifySearchForProduct(){
        Catalog catalog  =new Catalog();
        catalog.openCatalogPage();
        catalog.searchFor("headphones", "Showing 1 product");
    }

    @Test
    @DisplayName("Exercise 3: Assert the rows collection")
    void verifyResultsCollections(){
        Catalog catalog  =new Catalog();
        catalog.openCatalogPage();
        catalog.productRows(3);

        List<String> expected = List.of("FOOTWEAR", "BAGS", "ELECTRONICS");
        catalog.textsInOrder(expected);
        List<String> list2 = List.of("Running Shoes", "Travel Backpack", "Noise Canceling Headphones");
        assertEquals(list2, catalog.getTitles());

        catalog.filterUsingSelenide("FOOTWEAR",1);
    }

    @Test
    @DisplayName("Exercise 4: Extra")
    void test(){
        Catalog catalog  =new Catalog();
        catalog.openCatalogPage();
        catalog.productRows(3);

        List<String> expected = List.of("FOOTWEAR", "BAGS", "ELECTRONICS");
        catalog.textsInOrder(expected);
//        List<String> list2 = List.of("Shoes", "Backpack", "Headphones");
        List<String> list2 = List.of("Running Shoes", "Travel Backpack", "Noise Canceling Headphones");
        assertEquals(list2, catalog.getTitles());
    }

}
