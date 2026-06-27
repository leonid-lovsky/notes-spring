plugins {
    id("java")
    id("jacoco")
}

repositories {
    mavenCentral()
}

jacoco {
    toolVersion = "0.8.14"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}
