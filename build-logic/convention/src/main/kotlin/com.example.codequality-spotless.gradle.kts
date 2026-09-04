plugins {
    id("com.diffplug.spotless")
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        googleJavaFormat().aosp()
    }
}

tasks.named("compileJava") {
    dependsOn("spotlessApply")
}
