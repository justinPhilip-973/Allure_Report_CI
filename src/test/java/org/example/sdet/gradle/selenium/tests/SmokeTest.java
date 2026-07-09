package org.example.sdet.gradle.selenium.tests;

import org.example.sdet.gradle.selenium.support.Config;
import org.example.sdet.gradle.selenium.support.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

public class SmokeTest {
    private WebDriver driver;

    @BeforeEach
    void setup(){
        driver = DriverFactory.createChromeDriver();
    }

    @AfterEach
    void testShutter(){
        if(driver!=null){
            driver.quit();
        }
    }

    @Test
    @DisplayName("Product Catalog loads in a real Chrome session")
    void catalogLoads(){
        driver.get(Config.catalogURL());

        By catalogHeading= By.cssSelector("[data-test='catalog-title']");

        assertAll(
                () -> assertTrue(driver.getTitle().contains("Catalog")),
                () -> assertTrue(driver.findElement(catalogHeading).isDisplayed()),
                () -> assertEquals("Product Catalog", driver.findElement(catalogHeading).getText())
        );
    }


}
