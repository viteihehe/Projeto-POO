plugins {
    application
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")
}

sourceSets {
    getByName("main") {
        java.setSrcDirs(listOf("src"))
        resources.setSrcDirs(listOf("assets"))
    }
}

application {
    mainClass = "App"
}

