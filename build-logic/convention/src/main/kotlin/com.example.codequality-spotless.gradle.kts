plugins {
    id("com.diffplug.spotless")
}

spotless {
    lineEndings = com.diffplug.spotless.LineEnding.PRESERVE

    java {
        importOrder("java", "javax", "", "org.springframework")
    }
}

tasks.named("compileJava") {
    dependsOn("spotlessApply")
}
