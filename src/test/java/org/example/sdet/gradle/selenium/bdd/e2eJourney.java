package org.example.sdet.gradle.selenium.bdd;

import org.example.sdet.gradle.selenium.pages.CatalogPage;
import org.example.sdet.gradle.selenium.pages.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class e2eJourney {
    private final World world;

    public e2eJourney(World world){
        this.world = world;
    }

    @Given("the retail app is open")
    public void theRetailIsOpen(){
        world.home = new HomePage(world.driver);
        world.home.openHome();
    }

    @Given("I am on the login page")
    public void iIsInLoginPage(){
        world.login = world.home.openLogin();
    }

    @When("I enter valid credentials")
    public void iEnterValidCredentials(){
        world.login.enteringCredentials("customer@example.com","Password@123");
    }

    @When("I click on the login button")
    public void iClickOnLoginButton(){
        world.home = world.login.clickLoginButton();
    }

    @Then("I should be redirected to the home page")
    public void iShouldBeRedirectedToHomePage(){
        assertTrue(world.home.isThisHome().toLowerCase().contains("home"));
    }

    @Then("a welcome message should be displayed")
    public void aWelcomeMessageShouldBeDisplayed(){
        assertTrue(world.home.welcomeUser().toLowerCase().contains("customer user"));
    }
//------------------------------------------------------------------------------------

    @Given("I am logged into the retail app")
    public void iAmLoggedInRetailApp(){
        world.login = world.home.openLogin();
        world.login.enteringCredentials("customer@example.com","Password@123");
        world.home = world.login.clickLoginButton();
        assertTrue(world.home.amILoggedIn().getText().toLowerCase().contains("logged-in"));
    }

    @Given("I am on home page")
    public void iAmOnHomePage(){
        assertTrue(world.home.isThisHome().toLowerCase().contains("home"));
    }

    @When("I click on previewProducts")
    public void iClickOnPreviewProducts(){
        world.catalog = world.home.previewProducts();
    }

    @Then("I am in catalog page")
    public void theCatalogIsOpen(){
        assertTrue(world.catalog.isThisCatalog().toLowerCase().contains("catalog"));
    }
}
