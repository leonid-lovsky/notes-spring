plugins {
    id("com.github.spotbugs")
}

spotbugs {
    omitVisitors.add("FindReturnRef")
}
