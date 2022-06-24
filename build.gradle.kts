import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.kotlinx.kover") version("0.5.1") apply(true)
    id("org.sonarqube") version("3.3") apply(true)
}

group = "digital.pashabank"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()

    extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
        isDisabled = false
        binaryReportFile.set(file("$buildDir/custom/result.bin"))
        includes = listOf("*")
        excludes = listOf()
    }
}

kover {
    isDisabled = false
    coverageEngine.set(kotlinx.kover.api.CoverageEngine.JACOCO)
    jacocoEngineVersion.set("0.8.7")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val koverOutputDir = "${rootProject.buildDir}/kover-reports"

tasks.koverHtmlReport {
    isEnabled = true
    htmlReportDir.set(layout.buildDirectory.dir("${koverOutputDir}/html-result"))

    includes = listOf("*")
    excludes = listOf()
}

tasks.koverXmlReport {
    isEnabled = true
    xmlReportFile.set(layout.buildDirectory.file("${koverOutputDir}/result.xml"))

    includes = listOf("*")
    excludes = listOf()
}

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "ahajizade")
        property("sonar.projectKey", "ahajizade_cicdworkshop1")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${koverOutputDir}/result.xml"
        )
    }
}
