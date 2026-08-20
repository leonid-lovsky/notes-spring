plugins {
    id("com.diffplug.spotless")
}

spotless {
    lineEndings = com.diffplug.spotless.LineEnding.PRESERVE

    java {
        importOrder(
            "java",
            "javax",
            "com.example",
            "jakarta.persistence",
            "org.assertj",
            "org.jspecify",
            "org.junit",
            "org.testcontainers",
            "reactor.core",
            "",
            "org.springframework",
        )
    }
}

tasks.named("compileJava") {
    dependsOn("spotlessApply")
}
