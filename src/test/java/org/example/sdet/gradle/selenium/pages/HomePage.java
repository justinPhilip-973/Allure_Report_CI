package org.example.sdet.gradle.selenium.pages;

import org.example.sdet.gradle.selenium.support.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage{

    private static final By SIGN_IN = By.cssSelector(".button.primary[href='/login']");
    private static final By PREVIEW_PRODUCTS = By.cssSelector(".button.secondary[href='/catalog']");
    private static final By OPEN_SYNC_LAB = By.cssSelector(".button.secondary[href='/sync-lab']");
    private static final By WELCOME_MESSAGE = By.id("page-title");
    private static final By LOGGED_IN = By.className("eyebrow");

    public HomePage(WebDriver driver){
        super(driver);
    }


    public HomePage openHome(){
        driver.get(Config.homeURL());
        visible(SIGN_IN);
        return this;
    }

    public WebElement amILoggedIn(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGGED_IN));
        return driver.findElement(LOGGED_IN);

    }

    public LoginPage openLogin(){
        click(SIGN_IN);
        return new LoginPage(driver);
    }

    public String isThisHome(){
        return driver.getCurrentUrl();
    }

    public CatalogPage previewProducts(){
        click(PREVIEW_PRODUCTS);
        return new CatalogPage(driver);
    }

    public String welcomeUser(){
        return driver.findElement(WELCOME_MESSAGE).getText();
    }

//    public CheckoutPage proceed(){
//        click(CHECKOUT);
//        return new CheckoutPage(driver);
//    }
}
