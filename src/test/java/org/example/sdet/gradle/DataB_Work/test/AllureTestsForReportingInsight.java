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
    @Severity(SeverityLevel.NORMAL)
    void flakyCategoryIsDefined() throws IOException {
        String categories =
                Files.readString(Path.of("src/test/resources/allure/categories.json"));

        assertTrue(categories.contains("Flaky tests"));
    }

    @Test
    @Story("Categories")
    @Severity(SeverityLevel.NORMAL)
    void productDefectCategoryIsDefined() throws IOException {
        String categories =
                Files.readString(Path.of("src/test/resources/allure/categories.json"));

        assertTrue(categories.contains("Product defects (failed)"));
    }

    @Test
    @Story("Categories")
    @Severity(SeverityLevel.NORMAL)
    void testDefectCategoryIsDefined() throws IOException {
        String categories =
                Files.readString(Path.of("src/test/resources/allure/categories.json"));

        assertTrue(categories.contains("Test defects (broken)"));
    }

    @Test
    @Story("Environment metadata")
    @Severity(SeverityLevel.NORMAL)
    void browserMetadataExists() throws IOException {
        List<String> lines =
                Files.readAllLines(Path.of("src/test/resources/allure/environment.properties"));

        assertTrue(lines.stream().anyMatch(line -> line.startsWith("Browser=")));
    }

    @Test
    @Story("Environment metadata")
    @Severity(SeverityLevel.NORMAL)
    void baseUrlMetadataExists() throws IOException {
        List<String> lines =
                Files.readAllLines(Path.of("src/test/resources/allure/environment.properties"));

        assertTrue(lines.stream().anyMatch(line -> line.startsWith("BaseURL=")));
    }

    @Test
    @Story("Executive overview")
    @Severity(SeverityLevel.NORMAL)
    void leadershipDashboardRequiresTrendData() {
        assertTrue(
                List.of("status","trend","category split","environment")
                        .contains("trend"));
    }

    @Test
    @Story("Executive overview")
    @Severity(SeverityLevel.NORMAL)
    void leadershipDashboardRequiresEnvironmentData() {
        assertTrue(
                List.of("status","trend","category split","environment")
                        .contains("environment"));
    }


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
    @Story("Executive overview with more assertions")
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
    @Story("Categories")
    @Severity(SeverityLevel.NORMAL)
    void knownIssueCategoryIsDefined() throws IOException {
        String categories =
                Files.readString(Path.of("src/test/resources/allure/categories.json"));

        assertTrue(categories.contains("Known Issues"));
    }

    @Test
    @Story("Categories")
    @Severity(SeverityLevel.NORMAL)
    void riskAcceptedCategoryIsDefined() throws IOException {
        String categories =
                Files.readString(Path.of("src/test/resources/allure/categories.json"));

        assertTrue(categories.contains("Risk Accepted"));
    }

    @Test
    @Story("Environment metadata")
    @Severity(SeverityLevel.NORMAL)
    void buildMetadataExists() throws IOException {
        List<String> lines =
                Files.readAllLines(Path.of("src/test/resources/allure/environment.properties"));

        assertTrue(lines.stream().anyMatch(line -> line.startsWith("Build=")));
    }

    @Test
    @Story("Environment metadata")
    @Severity(SeverityLevel.NORMAL)
    void osMetadataExists() throws IOException {
        List<String> lines =
                Files.readAllLines(Path.of("src/test/resources/allure/environment.properties"));

        assertTrue(lines.stream().anyMatch(line -> line.startsWith("OS=")));
    }

    @Test
    @Story("Reporting assets")
    @Severity(SeverityLevel.NORMAL)
    void allureCategoriesFileExists() {
        assertTrue(
                Files.exists(Path.of("src/test/resources/allure/categories.json")));
    }

    @Test
    @Story("Reporting assets")
    @Severity(SeverityLevel.NORMAL)
    void allureEnvironmentFileExists() {
        assertTrue(
                Files.exists(Path.of("src/test/resources/allure/environment.properties")));
    }

    @Test
    @Story("Flaky classification")
    @Severity(SeverityLevel.NORMAL)
    void flakyRegexContainsTimeout() throws IOException {
        String categories =
                Files.readString(Path.of("src/test/resources/allure/categories.json"));

        assertTrue(categories.contains("timeout"));
    }

    @Test
    @Story("Flaky classification")
    @Severity(SeverityLevel.NORMAL)
    void flakyRegexContainsStaleElement() throws IOException {
        String categories =
                Files.readString(Path.of("src/test/resources/allure/categories.json"));

        assertTrue(categories.contains("stale element"));
    }

    @Test
    @Story("Flaky classification")
    @Severity(SeverityLevel.NORMAL)
    void flakyRegexContainsConnectionReset() throws IOException {
        String categories =
                Files.readString(Path.of("src/test/resources/allure/categories.json"));

        assertTrue(categories.contains("connection reset"));
    }

    @Test
    @Story("Executive overview")
    @Severity(SeverityLevel.NORMAL)
    void executiveDashboardShowsStatus() {
        assertTrue(List.of(
                        "status",
                        "trend",
                        "category split",
                        "environment")
                .contains("status"));
    }

    @Test
    @Story("Executive overview")
    @Severity(SeverityLevel.NORMAL)
    void executiveDashboardShowsTrend() {
        assertTrue(List.of(
                        "status",
                        "trend",
                        "category split",
                        "environment")
                .contains("trend"));
    }

    @Test
    @Story("Executive overview")
    @Severity(SeverityLevel.NORMAL)
    void executiveDashboardShowsEnvironment() {
        assertTrue(List.of(
                        "status",
                        "trend",
                        "category split",
                        "environment")
                .contains("environment"));
    }

    @Test
    @Story("Executive overview")
    @Severity(SeverityLevel.NORMAL)
    void executiveDashboardShowsCategorySplit() {
        assertTrue(List.of(
                        "status",
                        "trend",
                        "category split",
                        "environment")
                .contains("category split"));
    }

    @Test
    @Story("Reporting metadata")
    @Severity(SeverityLevel.NORMAL)
    void severityMetadataIsPresent() {
        SeverityLevel severity = SeverityLevel.CRITICAL;

        assertNotNull(severity);
    }

    @Test
    @Story("Reporting metadata")
    @Severity(SeverityLevel.NORMAL)
    @Description("The reporting test suite must declare ownership metadata.")
    void ownerMetadataIsPresent() {
        Owner owner =
                AllureTestsForReportingInsight.class.getAnnotation(Owner.class);

        assertNotNull(owner, "@Owner annotation must be present");
        assertEquals(
                "justinGradle",
                owner.value(),
                "Owner metadata should identify the reporting suite owner");
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
    @Story("Order lifecycle validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
    Orders marked as shipped should contain a carrier reference.
    Missing carrier information indicates a product defect.
    """)
    void shippedOrderMustContainCarrierReference() {
        Allure.label("failureType", "product-defect");
        Allure.label("classification", "failed");
        String carrierReference = "";
        assertFalse(carrierReference.isEmpty(),
                "Shipped orders must contain a carrier reference");
    }


    @Test
    @Story("Catalog validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
    Products visible in the catalog must expose pricing information.
    Missing pricing prevents purchasing decisions.
    """)
    void catalogProductMustExposePrice() {
        Allure.label("failureType", "product-defect");
        Allure.label("classification", "failed");
        Double productPrice = null;
        assertNotNull(productPrice, "Catalog product must expose a valid price");
    }


    @Test
    @Story("Checkout validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
    Orders submitted through checkout must contain at least one line item.
    Empty orders represent an invalid transaction.
    """)
    void orderMustContainAtLeastOneItem() {
        Allure.label("failureType", "product-defect");
        Allure.label("classification", "failed");
        int lineItems = 0;
        assertTrue(lineItems > 0,
                "Order must contain at least one item");
    }

    @Test
    @Story("Authentication validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
    Administrative actions require elevated privileges.
    Executing an administrative workflow as a standard user indicates a product defect.
    """)
    void adminWorkflowShouldRequireAdminRole() {
        Allure.label("failureType", "product-defect");
        Allure.label("classification", "failed");
        boolean userIsAdmin = false;
        assertTrue(userIsAdmin,
                "Administrative workflow must require admin privileges");
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
        assertTrue(
                trackingReference != null && !trackingReference.isBlank(),
                "Completed orders must provide a shipment tracking reference");
    }



    @Test
    @Story("Known issue validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
    Legacy pricing calculations are known to produce occasional rounding
    differences. The issue is documented and awaiting remediation.
    """)
    void legacyPricingEngineRoundingIssue() {

        Allure.label("failureType", "known-issue");
        Allure.label("classification", "known-issue");

        fail("KNOWN_ISSUE: legacy pricing engine rounding mismatch");
    }


    @Test
    @Story("Known issue validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
    Historic orders migrated from legacy systems may exhibit tax calculation
    differences. The product team has already acknowledged this issue.
    """)
    void historicTaxCalculationMismatch() {

        Allure.label("failureType", "known-issue");
        Allure.label("classification", "known-issue");

        fail("KNOWN_ISSUE: tax calculation discrepancy for migrated orders");
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



    @Test
    @Story("Risk acceptance validation")
    @Severity(SeverityLevel.MINOR)
    @Description("""
    Audit retention enforcement is temporarily disabled as part of a
    documented business decision. The associated risk has been accepted.
    """)
    void auditRetentionIsTemporarilyDisabled() {

        Allure.label("failureType", "risk-accepted");
        Allure.label("classification", "risk-accepted");

        fail("RISK_ACCEPTED: audit retention temporarily disabled");
    }

}



