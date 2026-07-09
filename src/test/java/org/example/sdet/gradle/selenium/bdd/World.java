package org.example.sdet.gradle.selenium.bdd;

import org.example.sdet.gradle.selenium.pages.*;
import org.example.sdet.gradle.selenium.pages.components.Header;
import org.openqa.selenium.WebDriver;

public class World {
    public WebDriver driver;
    public HomePage home;
    public LoginPage login;
    public CatalogPage catalog;
    public ProductPage product;
    public CartPage cart;
    public CheckoutPage checkout;


    public Header header(){
        return new Header(driver);
    }
}
