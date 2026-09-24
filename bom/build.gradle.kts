plugins {
    `java-platform`
    alias(libs.plugins.maven.publish)
}

dependencies {
    constraints {
        api(project(":core:common"))
        api(project(":core:settings"))
        api(project(":core:security"))
        api(project(":feature:user"))
        api(project(":feature:clientuser"))
        api(project(":feature:managementuser"))
    }
}
