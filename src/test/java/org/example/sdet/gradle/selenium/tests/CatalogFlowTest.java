package org.example.sdet.gradle.selenium.tests;

import org.example.sdet.gradle.selenium.support.Config;
import org.example.sdet.gradle.selenium.support.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class CatalogFlowTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup(){
        driver = DriverFactory.createChromeDriver();
        wait =new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(Config.catalogURL());
    }

    @AfterEach
    void testShutter(){
        if(driver!=null) {
            driver.quit();
        }
    }

    private static final By SEARCH_BOX= By.cssSelector("[data-test='search-input']");
    private static final By PRODUCT_CARDS= By.cssSelector("[data-test='product-card']");

    private static final By FIRST_PRODUCT_CARD= By.xpath("(//*[@data-test='product-card'])[1]");
    private static final By FIRST_PRODUCT_CARD_LINK= By.cssSelector("[data-test='product-card'] a");

    private static final By RESULT_COUNT= By.cssSelector("[data-test='catalog-result-count']");
    private static final By PRODUCT_TITLE= By.cssSelector("[data-test='product-title']");
    private static final By PRODUCT_PRICE= By.cssSelector("[data-test='product-price']");
    private static final By PRODUCT_LINK= By.cssSelector("[data-test='product-card'] a");
    private static final By DETAIL_NAME= By.cssSelector("[data-test='detail-name']");
    private static final By ADD_TO_CART= By.cssSelector("[data-test='add-to-cart']");
    private static final By CART_COUNT= By.cssSelector("[data-test='cart-count']");
    private static final By SORT_SELECT= By.cssSelector("[data-test='sort-select']");
    private static final By EMPTY_SEARCH_RESULT= By.cssSelector("[data-test='empty-search']");
    private static final By QUICK_VIEW= By.cssSelector("[data-test='quick-view']");
    private static final By SIZE_GUIDE= By.cssSelector(".product-actions a");
    private static final By SIZE_GUIDE_TITLE= By.id("size-guide-title");


    @Test
    @DisplayName("Test 1: Search and Validate Retail Products")
    void searchShowsMatchingProducts(){
        search("headphones", "Showing 1 product");

        List<WebElement> cards = driver.findElements(PRODUCT_CARDS);
        assertEquals(1,cards.size(),"headphones search should return one product");

        for (WebElement card: cards){
            String title = card.findElement(PRODUCT_TITLE).getText();
            assertAll(
                    () -> assertTrue(title.toLowerCase().contains("headphone"), "unrelated result product "+title),
                    () -> assertTrue(card.findElement(PRODUCT_PRICE).isDisplayed(), "price missing for: "+title)
            );
        }
    }

    @Test
    @DisplayName("Test 2: Product detail opens and add-to-cart updates the cart badge")
    void addtoCart_UpdatesBadge(){
        wait.until(ExpectedConditions.elementToBeClickable(PRODUCT_LINK)).click();

        WebElement detailName = wait.until(ExpectedConditions.visibilityOfElementLocated(DETAIL_NAME));
        assertAll(
                () -> assertTrue(driver.getCurrentUrl().contains("/product/")),
                () -> assertFalse(detailName.getText().isBlank())
        );

        wait.until(ExpectedConditions.elementToBeClickable(ADD_TO_CART)).click();
        wait.until(ExpectedConditions.urlContains("/cart"));
        wait.until(ExpectedConditions.textToBe(CART_COUNT,"1"));

    }


    @Test
    @DisplayName("Test 3: Dropdown Select re-renders the cards in Ascending")
    void priceFilterUpdatesProductList(){
        WebElement oldFirstcard= wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_CARDS));

        new Select(driver.findElement(SORT_SELECT)).selectByVisibleText("Price: Low to High");

        wait.until(ExpectedConditions.stalenessOf(oldFirstcard));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(PRODUCT_CARDS,0));

        List <Integer> prices = driver.findElements(PRODUCT_PRICE).stream()
                        .map((element) -> Integer.parseInt(element.getText().replaceAll("[^0-9]", "")))
                                .toList();

        assertEquals(prices.stream().sorted().toList(), prices);
    }

    @Test
    @DisplayName("Test 4: Search with no matching product and return empty result message")
    void searchNonExistentProductShowsNoResults(){
        search("abcdefg-12466", "Showing 0 products");

        WebElement emptySearch=wait.until(ExpectedConditions.visibilityOfElementLocated(EMPTY_SEARCH_RESULT));
        assertAll(
                () -> assertTrue(driver.findElements(PRODUCT_CARDS).isEmpty(), "cards should be hidden for when empty search"),
                () -> assertEquals("No products found", emptySearch.findElement(
                        By.tagName("h2")).getText()
                )
        );
    }

    @Test
    @DisplayName("Test 5: Hovering the cursor on product card and viewing it's QuickView")
    void productShowsQuickViewOnPreview(){
//        WebElement firstcard= wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_PRODUCT_CARD));
        WebElement firstcard= wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(PRODUCT_CARDS)).get(0);

        new Actions(driver).moveToElement(firstcard).pause(Duration.ofSeconds(1)).perform();
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(QUICK_VIEW));
//        wait.until((ignored)-> firstcard.findElement(QUICK_VIEW)).getText();

        System.out.println(firstcard.getAttribute("outerHTML"));
        WebElement quickView =
                firstcard.findElement(QUICK_VIEW);

        System.out.println(quickView.isDisplayed());
        System.out.println(quickView.getText());
        assertEquals("Quick view ready", firstcard.findElement(QUICK_VIEW).getText());
    }

    @Test
    @DisplayName("Test 6: Opening size guide new tab and then switching back to Product Page")
    void sizeGuideOpensInNewTabAndswitchToProductPage(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_LINK)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(DETAIL_NAME));
        assertTrue(driver.findElement(DETAIL_NAME).getText().contains("Running"));

        String productTab= driver.getWindowHandle();

        wait.until(ExpectedConditions.elementToBeClickable(SIZE_GUIDE)).click();

        for(String tab : driver.getWindowHandles()){
            if(! tab.equals(productTab)){
                driver.switchTo().window(tab);
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(SIZE_GUIDE_TITLE));

        assertTrue(driver.getCurrentUrl().contains("size-guide"));
        assertTrue(driver.findElement(SIZE_GUIDE_TITLE).getText().contains("Size Guide"));

        String current_tab= driver.getWindowHandle();

        if(! current_tab.equals(productTab)){
            driver.switchTo().window(productTab);

        }

        assertTrue(driver.getCurrentUrl().contains("running-shoes"));
    }

    @Test
    @DisplayName("Test 7: Product Page to Promo Signup Frame")
    void inProductpageTAB_to_PromoSignupFrames() {
        wait.until(ExpectedConditions.elementToBeClickable(PRODUCT_LINK)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(DETAIL_NAME));
        assertTrue(driver.findElement(DETAIL_NAME).getText().contains("Running"));

        WebElement PROMO_FRAME = driver.findElement(new By.ByCssSelector(".promo-frame"));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", PROMO_FRAME
        );
//        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(PROMO_FRAME));

        driver.switchTo().frame(PROMO_FRAME);

        WebElement label= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label")));
        assertTrue(label.getText().contains("Email"));

        WebElement email = driver.findElement(By.name("email"));
        email.clear();
        email.sendKeys("abc@retailApp.com");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("promo-status"))).getText().contains("Thanks"));
        }



        private void search(String query, String expectedResultcount){
        WebElement searchInput=wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_BOX));
        searchInput.clear();
        searchInput.sendKeys(query);
        searchInput.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.textToBe(RESULT_COUNT, expectedResultcount));
    }
}
