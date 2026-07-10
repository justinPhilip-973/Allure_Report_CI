plugins {
    java
}

group = "com.ust.sdet"
version = "0.1.0"

val seleniumVersion = "4.45.0"
val selenideVersion = "7.16.2"
val junitVersion = "5.14.4"
val cucumberVersion = "7.34.3"
val allureVersion = "2.33.0"
val extentVersion = "5.1.2"
val extentCucumberAdapterVersion = "1.14.0"
val slf4jVersion = "2.0.17"
val testcontainersVersion = "1.20.6"
//val testcontainersVersion = "2.0.5"
val flywayVersion = "10.22.0"
//val postgresqlVersion = "42.7.4"
val mysqlLibVersion = "9.4.0"



java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation(platform("io.cucumber:cucumber-bom:$cucumberVersion"))
    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation(platform ( "org.testcontainers:testcontainers-bom:$testcontainersVersion" ))

    // Source: https://mvnrepository.com/artifact/io.github.cdimascio/java-dotenv
    testImplementation("io.github.cdimascio:java-dotenv:5.2.2")

    testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")
    testImplementation("com.codeborne:selenide:$selenideVersion")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
    testImplementation("io.cucumber:cucumber-picocontainer")
    testImplementation("org.junit.platform:junit-platform-suite")

    testImplementation("com.aventstack:extentreports:$extentVersion")
    testImplementation("tech.grasshopper:extentreports-cucumber7-adapter:$extentCucumberAdapterVersion")
    testImplementation("org.slf4j:slf4j-simple:$slf4jVersion")

    testImplementation("io.qameta.allure:allure-cucumber7-jvm")
    testImplementation("io.qameta.allure:allure-junit5")

//    testImplementation("org.testcontainers:testcontainers-postgresql:$testcontainersVersion")
//    testImplementation("org.flywaydb:flyway-database-postgresql:${flywayVersion}")
//    testImplementation("org.postgresql:postgresql:${postgresqlVersion}")

    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:mysql:$testcontainersVersion")

    testImplementation("org.flywaydb:flyway-core:$flywayVersion")
    testImplementation("org.flywaydb:flyway-mysql:$flywayVersion")

    testImplementation("com.mysql:mysql-connector-j:$mysqlLibVersion")

}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(17)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("baseUrl", providers.gradleProperty("baseUrl").orElse("http://localhost:5173").get())
    systemProperty("headless", providers.gradleProperty("headless").orElse("false").get())
    systemProperty("browser", providers.gradleProperty("browser").orElse("chrome").get())
    systemProperty("build.label", providers.gradleProperty("buildLabel").orElse("gradle-local").get())
    systemProperty("cucumber.publish.quiet", "true")
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
    }
}

fun Test.useProjectTestClasses() {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
}

tasks.test {
    description = "Runs the default classroom-safe Gradle checks without launching a browser."
    include("**/Day4SolidStructureTest.class")
    include("**/W6D1RefactoringStructureTest.class")
    maxParallelForks = 1
}

val testContainerCITests by tasks.registering(Test::class) {
    description = "Runs the safe CI for Week 6 Day 3."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/OrderTest.class")
}

val allureInsightTests by tasks.registering(Test::class) {
    description = "Runs the safe CI for Week 6 Day 4."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/AllureTestsForReportingInsight.class")
}

tasks.register("runAllureReportTestsFor_CI"){
    description = "Running all "
    group = "verification"

    dependsOn(
        testContainerCITests,
        allureInsightTests
    )
}

val w6d1StructureTest by tasks.registering(Test::class) {
    description = "Runs the safe no-browser Week 6 Day 1 structure checks."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/W6D1RefactoringStructureTest.class")
}

val w6d1CheckoutTest by tasks.registering(Test::class) {
    description = "Runs the optional browser Week 6 Day 1 checkout proof."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/W6D1RefactoredCheckoutFlowTest.class")
    maxParallelForks = 1
}

val cucumberSmoke by tasks.registering(Test::class) {
    description = "Runs Cucumber smoke scenarios through the Gradle JUnit Platform."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/RunCucumberTest.class")
    systemProperty("cucumber.filter.tags", "@smoke")
    maxParallelForks = 1
}
val cucumberRegression by tasks.registering(Test::class) {
    description = "Runs Cucumber smoke scenarios through the Gradle JUnit Platform."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/RunCucumberTest.class")
    systemProperty("cucumber.filter.tags", "@regression")
    maxParallelForks = 1
}
val cucumberExercise by tasks.registering(Test::class) {
    description = "Runs Cucumber smoke scenarios through the Gradle JUnit Platform."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/RunCucumberTest.class")
    systemProperty("cucumber.filter.tags", "@exercise")
    maxParallelForks = 1
}
val cucumberE2ESmoke by tasks.registering(Test::class) {
    description = "Runs the end-to-end Cucumber journey scenarios."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/RunE2ECucumber.class")
    systemProperty("cucumber.filter.tags", "@smoke")
    maxParallelForks = 1
}

val allCucumber by tasks.registering(Test::class) {
    description = "Runs Cucumber smoke scenarios through the Gradle JUnit Platform."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/RunAllCucumberTest.class")
    maxParallelForks = 1
}

val parallelStructureTest by tasks.registering(Test::class) {
    description = "Demonstrates Gradle test forks with no-browser checks."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/W6D1RefactoringStructureTest.class")
    maxParallelForks = Runtime.getRuntime().availableProcessors().coerceAtMost(2)
}

val refactoringCheck by tasks.registering(Test::class) {
    description = "Validates that refactoring rules and code structure requirements are satisfied."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/refactoringCheck.class")
    maxParallelForks = 1
}

val refactoredCheck by tasks.registering(Test::class) {
    description = "Verifies the behavior of the refactored implementation remains correct."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/refactoredCheck.class")
    maxParallelForks = 1
}

val catalogPomTest by tasks.registering(Test::class) {
    description = "Runs Catalog Page Object Model validation tests."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/CatalogPOMTests.class")
    maxParallelForks = 1
}

val catalogFlowTest by tasks.registering(Test::class) {
    description = "Runs end-to-end Catalog workflow and navigation tests."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/CatalogFlowTest.class")
    maxParallelForks = 1
}
val catalogTest by tasks.registering(Test::class) {
    description = "Runs end-to-end Catalog workflow using Selenide."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/CatalogTest.class")
    maxParallelForks = 1
}
val smokeTest by tasks.registering(Test::class) {
    description = "Runs end-to-end Catalog workflow using Selenide."
    group = "verification"
    useProjectTestClasses()
    useJUnitPlatform()
    include("**/SmokeTest.class")
    maxParallelForks = 1
}

tasks.register("w6d2BuildSummary") {
    description = "Prints the Week 6 Day 2 Maven to Gradle command map."
    group = "help"
    doLast {
        println(
            """
            W6D2 Build Tooling Summary
            Maven compile: mvn clean test-compile
            Gradle compile: ./gradlew clean testClasses
            Maven structure: mvn clean -Dtest=W6D1RefactoringStructureTest test
            Gradle structure: ./gradlew clean w6d1StructureTest
            Gradle smoke: ./gradlew cucumberSmoke -Pheadless=true
            Gradle scan: ./gradlew w6d1StructureTest --scan
            """.trimIndent()
        )
    }
}


tasks.register("runAllTests"){
    description = "Running all Selenium Cucumber tests, Selenide Catalog and, Refactoring Test."
    group = "verification"

    dependsOn(
        smokeTest,
        catalogPomTest,
        catalogFlowTest,
        cucumberSmoke,
        cucumberRegression,
        cucumberExercise,
        cucumberE2ESmoke,
        allCucumber,
        refactoringCheck,
        refactoredCheck,
        catalogTest
    )
}
