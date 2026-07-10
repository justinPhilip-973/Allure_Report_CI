package org.example.sdet.gradle.DataB_Work.test;

import io.qameta.allure.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Framework Hardening")
@Feature("Reporting Insights")
@Owner("justinGradle")
public class AllureTestsForReportingInsight {
    @Test
    @Story("Categories")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Categories split flaky, test and product failures so each bucket has an owner.")
    void categoriesPutSpecificFlakyRuleBeforeGenericBuckets() throws IOException {
        String categories =
                Files.readString(Path.of("src/test/resources/allure/categories.json"));

        int flakyIndex = categories.indexOf("\"Flaky tests\"");
        int testDefectIndex = categories.indexOf("\"Test defects (broken)\"");
        int productDefectIndex = categories.indexOf("\"Product defects (failed)\"");

        assertTrue(flakyIndex >= 0, "Flaky category must exist");
        assertTrue(testDefectIndex > flakyIndex,
                "Specific flaky rule must run before generic broken bucket");
        assertTrue(productDefectIndex > flakyIndex,
                "Specific flaky rule must run before generic failed bucket");
        assertTrue(categories.contains("\"flaky\": true"));
        assertTrue(categories.contains("timeout|stale element|connection reset"));
    }
    @Test
    @Story("Environment metadata")
    @Severity(SeverityLevel.NORMAL)
    @Description("Environment metadata answers which browser, base URL and build produced the report.")
    void environmentTemplateCarriesRunContext() throws IOException {
        List<String> lines =
                Files.readAllLines(Path.of("src/test/resources/allure/environment.properties"));

        assertTrue(lines.stream().anyMatch(line -> line.startsWith("Browser=")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("BaseURL=")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("Build=")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("OS=")));
    }
    @Test
    @Story("Executive overview")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The leadership view is the Overview page once categories, history, severity and environment exist.")
    void executiveViewNeedsFourSignals() {
        List<String> signals = List.of(
                "status",
                "trend",
                "category split",
                "environment"
        );

        assertEquals(4, signals.size());
        assertTrue(signals.contains("trend"));
        assertTrue(signals.contains("category split"));
    }

    @Test
    @Story("Timeout instability")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            External service latency exceeded the agreed response threshold.
            The failure should be classified as a flaky infrastructure issue
            rather than a product defect.
            """)
    void reportingServiceTimeoutShouldBeClassifiedAsFlaky() {
        Allure.label("failureType", "timeout");
        Allure.label("classification", "flaky");
        throw new RuntimeException(
                "timeout while waiting for reporting service response");
    }

    @Test
    @Story("Stale element instability")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            The user interface refreshed while the automation framework was
            interacting with a previously located element. This represents
            a transient automation instability.
            """)
    void dynamicPageRefreshCanTriggerStaleElementFailure() {
        Allure.label("failureType", "stale-element");
        Allure.label("classification", "flaky");
        throw new RuntimeException(
                "stale element reference while validating order status");
    }

    @Test
    @Story("Connection reset instability")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            A downstream dependency unexpectedly terminated the active network
            session. This type of infrastructure interruption should be
            classified as a flaky execution issue.
            """)
    void downstreamGatewayConnectionResetShouldBeFlaky() {
        Allure.label("failureType", "connection-reset");
        Allure.label("classification", "flaky");
        throw new RuntimeException(
                "connection reset during metrics synchronization");
    }

    @Test
    @Story("Business rule validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Orders reaching the COMPLETED state must expose a shipment tracking
            reference. Missing tracking data indicates a product defect.
            """)
    void completedOrderMustExposeTrackingReference() {
        Allure.label("failureType", "product-defect");
        Allure.label("classification", "failed");
        String trackingReference = "";
        String check = "";
//        assertTrue(
//                trackingReference != null && !trackingReference.isBlank(),
//                "Completed orders must provide a shipment tracking reference");
        assertFalse(check.isEmpty());
    }

    @Test
    @Story("Framework dependency validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            The reporting adapter must be initialized before test execution.
            Failure to construct framework dependencies represents a test
            defect because the product was never exercised.
            """)
    void frameworkDependencyShouldBeInitializedBeforeExecution() {
        Allure.label("failureType", "framework-initialization");
        Allure.label("classification", "broken");
        Object num = null;
        num.toString();
    }

    @Test
    @Disabled("Demonstration: external analytics warehouse unavailable in CI")
    @Story("Conditional execution")
    @Severity(SeverityLevel.MINOR)
    @Description("""
            Historical analytics validation requires an external warehouse
            environment and is intentionally skipped in the standard CI
            execution path.
            """)
    void historicalAnalyticsValidationRequiresDedicatedEnvironment() {
        Allure.label("failureType", "environment-dependency");
        Allure.label("classification", "skipped");
    }


}



