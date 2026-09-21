plugins {
    id("com.diffplug.spotless")
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        googleJavaFormat()
        leadingTabsToSpaces()
    }
}

tasks.named("compileJava") {
    dependsOn("spotlessApply")
}

// https://github.com/diffplug/spotless/issues/3067: clean closes shared formatter classloaders still used by spotless
tasks.withType<com.diffplug.gradle.spotless.SpotlessTask>().configureEach {
    mustRunAfter(provider { rootProject.allprojects.mapNotNull { it.tasks.findByName("clean") } })
}
