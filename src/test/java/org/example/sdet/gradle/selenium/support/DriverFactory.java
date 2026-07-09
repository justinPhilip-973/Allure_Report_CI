package org.example.sdet.gradle.selenium.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createChromeDriver() {

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        if (Config.headless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440, 900");
        return new ChromeDriver(options);
    }

//    public static WebDriver createChromeDriver(){
//        return createDriver();
//    }

//    public static WebDriver createDriver(){
//        ChromeOptions options = new ChromeOptions();
//        if (Config.headless()) {
//            options.addArguments("--headless=new");
//        }
//        options.addArguments("--window-size=1440,900");
//        if (Config.gridEnabled()){
//            try {
//                return new RemoteWebDriver((URI.create(Config.gridURl())))
//            }
//        }
//    }


}

