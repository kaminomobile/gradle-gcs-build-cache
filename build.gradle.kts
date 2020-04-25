import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.3.70"
    id("com.github.hierynomus.license") version "0.15.0"
    id("com.gradle.plugin-publish") version "0.11.0"
    `kotlin-dsl`
    `maven-publish`
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

group = "si.kamino"
version = "1.0.0-SNAPSHOT"

repositories {
    jcenter()
    google()
}

dependencies {
    implementation("com.google.cloud:google-cloud-storage:1.107.0")

    // Lock guava version as it causes conflict with android plugin. Android pluing uses -jre version, google cloud lib
    //  uses -android version. And android version has some classes that are not in -jre version but are actually part
    //  of java, and therefore later cannot be cast to it. (-jre is compiled against 1.8, -android is compiled against 1.7)
    implementation("com.google.guava:guava") {
        version {
            strictly("27.0.1-jre")
        }
    }
    implementation(kotlin("stdlib-jdk8"))
}

//ktlint {
//    reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
//}

gradlePlugin {
    plugins {
        create("gcsBuildCache") {
            id = "si.kamino.gradle-gcs-build-cache"
            implementationClass = "si.kamino.gradle.caching.GCSBuildCachePlugin"
            displayName = "GCS Build Cache"
            description = "A Gradle build cache implementation that uses Google Cloud Storage (GCS) to store the build artifacts. Since this is a settings plugin the build script snippets below won't work. Please consult the documentation at Github."
        }
    }
}

pluginBundle {
    website = "https://github.com/kaminomobile/gradle-gcs-build-cache"
    vcsUrl = "https://github.com/kaminomobile/gradle-gcs-build-cache.git"
    tags = listOf("build-cache", "gcs", "Google Cloud Storage", "cache")
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
