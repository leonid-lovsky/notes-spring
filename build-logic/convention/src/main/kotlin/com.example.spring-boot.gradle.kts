import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("com.example.base")
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}
