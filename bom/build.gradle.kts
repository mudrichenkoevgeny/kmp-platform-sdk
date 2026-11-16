plugins {
    `java-platform`
    alias(libs.plugins.kmp.platform.publish)
}

dependencies {
    constraints {
        api(project(":core:common"))
        api(project(":core:settings"))
        api(project(":core:security"))
        api(project(":core:storage"))
        api(project(":testing"))
        api(project(":feature:user"))
        api(project(":feature:clientuser"))
        api(project(":feature:managementuser"))
    }
}
