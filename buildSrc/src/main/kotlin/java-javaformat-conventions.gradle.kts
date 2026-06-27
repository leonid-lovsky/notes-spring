plugins {
    id("java")
    id("io.spring.javaformat")
    id("checkstyle")
}

repositories {
    mavenCentral()
}

checkstyle {
    toolVersion = "9.3"
}
