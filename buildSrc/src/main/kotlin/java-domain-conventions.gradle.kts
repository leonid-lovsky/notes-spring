plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jspecify:jspecify:0.3.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
