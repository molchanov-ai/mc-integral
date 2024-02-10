// For `KotlinCompile` task below
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10" // Kotlin version to use
    application // Application plugin. Also see 1️⃣ below the code
}

group = "org.molchanov.ai" // A company name, for example, `org.jetbrains`
version = "1.0-SNAPSHOT" // Version to assign to the built artifact

repositories { // Sources of dependencies. See 2️⃣
    mavenCentral() // Maven Central Repository. See 3️⃣,
    maven("https://repo.kotlin.link")
//    maven {
//        url = uri("https://maven.sciprog.center/spc")
//    }
}

val letsPlotVersion = extra["letsPlot.version"] as String
val letsPlotKotlinVersion = extra["letsPlotKotlin.version"] as String
val slf4jVersion = extra["slf4j.version"] as String

dependencies { // All the libraries you want to use. See 4️⃣
    // Copy dependencies' names after you find them in a repository
    testImplementation(kotlin("test")) // The Kotlin test library
//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    api("space.kscience:kmath-core:0.3.1")
//    implementation("space.kscience:kmath-stat:0.4.0-dev-1")
    implementation("space.kscience:kmath-stat:0.3.1")

    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:$letsPlotKotlinVersion")
    implementation("org.jetbrains.lets-plot:platf-awt-jvm:$letsPlotVersion")
    implementation("org.jetbrains.lets-plot:deprecated-in-v4-jvm:$letsPlotVersion")

    //    For PNG export demo
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:$letsPlotVersion")

    implementation("org.slf4j:slf4j-simple:$slf4jVersion")  // Enable logging to console
}

tasks.test { // See 5️⃣
    useJUnitPlatform() // JUnitPlatform for tests. See 6️⃣
}

kotlin { // Extension for easy setup
    jvmToolchain(17) // Target version of generated JVM bytecode. See 7️⃣
}

application {
    mainClass.set("MainKt") // The main class of the application
}