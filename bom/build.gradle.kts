plugins {
    `java-platform`
    alias(libs.plugins.kmp.platform.publish)
}

dependencies {
    constraints {
        api(project(":core"))
        api(project(":testing"))
        api(project(":feature:user"))
        api(project(":feature:clientuser"))
        api(project(":feature:managementuser"))
    }
}
