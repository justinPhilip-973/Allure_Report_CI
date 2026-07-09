package org.example.sdet.gradle.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage{

    private static final By CART_PAGE = By.cssSelector("[data-test='cart-page']");
    private static final By CART_LINES = By.cssSelector("[data-test='cart-line']");
    private static final By CART_TOTAL = By.cssSelector("[data-test='cart-total']");
    private static final By CHECKOUT = By.cssSelector("[data-test='checkout-button']");

    public CartPage(WebDriver driver){
        super(driver);
        visibleElements(CART_PAGE);
    }

    public int lineCount(){
        return elements(CART_LINES).size();
    }

    public String total(){
        return text(CART_TOTAL);
    }

    public CheckoutPage proceed(){
        click(CHECKOUT);
        System.out.println("cartPage proceed function:"+driver.getCurrentUrl());
        return new CheckoutPage(driver);
    }



}
