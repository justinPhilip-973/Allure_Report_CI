package org.example.sdet.gradle.selenium.secure;

import io.github.cdimascio.dotenv.Dotenv;

public class creds {
    private static final Dotenv dotenv = Dotenv.load();

    //.env file contents
    public static final String selenium_url =  dotenv.get("Selenium_URL");

}
