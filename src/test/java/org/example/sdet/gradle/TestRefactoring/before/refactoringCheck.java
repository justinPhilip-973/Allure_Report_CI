package org.example.sdet.gradle.TestRefactoring.before;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.By.cssSelector;

//@Test(invocation_count)
public class refactoringCheck {
    public WebDriver driver;
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    public void orderTotalIsCorrect(){
        driver.get("http://localhost:5173/catalog");
        driver.findElement((cssSelector(".product-card a"))).click();

        String priceText = driver.findElement(By.cssSelector(".price")).getText();
        int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
        System.out.println(price);

        driver.findElement((By.id("quantity"))).clear();
        driver.findElement((By.id("quantity"))).sendKeys("3", Keys.ENTER);

        final By ADD_TO_CART = By.cssSelector("[data-test='add-to-cart']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_TO_CART));

        String orderTotal_Text = driver.findElement(By.cssSelector("[data-testid='order-total']")).getText();
        int orderTotal = Integer.parseInt(orderTotal_Text.replaceAll("[^0-9]", ""));

        assertEquals(price*3, orderTotal);
    }
}
