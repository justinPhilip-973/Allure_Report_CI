package org.example.sdet.gradle.TestRefactoring.to_after;

import org.example.sdet.gradle.selenium.support.Config;
import org.example.sdet.gradle.selenium.support.DriverFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class refactoredCheck {
    private static final By CHECKOUT_BUTTON = By.xpath("//button[text()='Place order']");
    private static final By CHECKOUT_TOTAL = By.cssSelector("[data-testid='checkout-total']");


    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.createChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(Config.catalogURL());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("The Order total is correct")
    void checkCheckoutTotal(){

    }

}