package org.example.sdet.gradle.selenium.tests;

import org.example.sdet.gradle.selenium.pages.CartPage;
import org.example.sdet.gradle.selenium.pages.CatalogPage;
import org.example.sdet.gradle.selenium.pages.ProductPage;
import org.example.sdet.gradle.selenium.support.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CatalogPOMTests {
    private WebDriver driver;

    @BeforeEach
    void setup(){
        driver= DriverFactory.createChromeDriver();
    }

    @AfterEach
    void testShutter(){
        if(driver!=null){
            driver.quit();
        }
    }

    @Test
    @DisplayName("Test 1A: POM Searching and returns the product price & title")
    void searchFindsOnlyMatchingProducts(){
        CatalogPage catalog = new CatalogPage(driver)
                .open()
                .searchFor("headphones", "Showing 1 product");


        List<String> titles = catalog.titles();

        List<Integer> prices= catalog.prices();

        assertAll(
                () -> assertFalse(titles.isEmpty(), "search result for valid search should not be empty"),
                () -> assertTrue(titles.stream().allMatch(
                        (title) -> title.toLowerCase().contains("headphones")),
                        "search results should be related to headphones"),
                () -> assertFalse(prices.isEmpty(),"search result for valid search should not be empty"),
                () -> assertTrue(prices.stream().allMatch(
                                (paisa) -> paisa.equals(7999)),
                        "search results should have valid price")
        );
    }

    @Test
    @DisplayName("Text 1B: POM Searching MOUSE")
    void searchFindsMousereturnsEmptyResult(){
        CatalogPage mouse_catalog = new CatalogPage(driver)
                .open()
                .searchFor("mouse", "Showing 0 products");

        assertTrue(mouse_catalog.emptySearch().toLowerCase().contains("no products found"));
    }

    @Test
    @DisplayName("Text 2: Sort Based on Price")
    void sortProductBasedOnPrice(){
        CatalogPage catalog=new CatalogPage(driver)
                .open()
                .sortBy("Price: Low to High");

        List<Integer> prices = catalog.prices();

        assertEquals(prices.stream().sorted().toList(), prices);
    }

    @Test
    @DisplayName("Text 3: Full Happy Journey")
    void full_happy_journey(){
        CatalogPage catalog = new CatalogPage(driver)
                .open()
                .searchFor("headphones", "Showing 1 product");

        ProductPage product = catalog.openFirstProduct();
        assertTrue(product.name().toLowerCase().contains("headphones"));

        CartPage cart = product.addToCart();
        cart.header().cartBadge().expectedCount(1);

        assertAll(
                () -> assertEquals(1, cart.lineCount()),
                () -> assertFalse(cart.total().isBlank())
        );

        String confirmation = cart.proceed()
                .placeOrder().confirmationText();

        assertTrue(confirmation.toLowerCase().contains("confirmed"));

    }




}
