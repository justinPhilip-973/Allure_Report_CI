package org.example.sdet.gradle.selenium.pages;
import org.example.sdet.gradle.selenium.support.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public class LoginPage extends BasePage{

    private static final By EMAIL = By.id("email");
    private static final By PASSWORD = By.id("password");
    private static final By SIGN_IN = By.cssSelector("button[type='submit']");
    private static final By ERROR_MESSAGE =
            By.xpath("//*[contains(text(),'Invalid credentials')]");
    private static final By REMEMBER_ME = By.cssSelector("input[name='rememberMe']");


    public LoginPage(WebDriver driver) {
        super(driver);
    }


    public LoginPage enteringCredentials(String email, String password) {
        type(EMAIL, email);
        type(PASSWORD, password);
        check(REMEMBER_ME);
        return this;
    }

    public HomePage clickLoginButton(){
        click(SIGN_IN);
        wait.until(driver -> Objects.requireNonNull(driver.getCurrentUrl()).contains("home"));
        return new HomePage(driver);
    }

    public LoginPage loginExpectingFailure(String email, String password) {
        type(EMAIL, email);
        type(PASSWORD, password);
        click(SIGN_IN);
        visible(ERROR_MESSAGE);
        return this;
    }

    public String errorMessage() {
        return text(ERROR_MESSAGE);
    }
}

