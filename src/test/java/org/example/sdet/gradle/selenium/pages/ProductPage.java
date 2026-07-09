package org.example.sdet.gradle.selenium.pages;

import org.example.sdet.gradle.selenium.pages.components.ProductCard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage{
    private static final By DETAIL_NAME = By.cssSelector("[data-test='detail-name']");
    private static final By ADD_TO_CART = By.cssSelector("[data-test='add-to-cart']");

    public ProductPage(WebDriver driver){
        super(driver);
        visible(DETAIL_NAME);
    }

    public String name(){
        return text(DETAIL_NAME);
    }

    public CartPage addToCart(){
        click(ADD_TO_CART);
        wait.until(ExpectedConditions.urlContains("/cart"));
        return new CartPage(driver);
    }


}
