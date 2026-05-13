plugins {
    `java-platform`
}

dependencies {
    constraints {
        api(project(":core:common"))
        api(project(":core:settings"))
        api(project(":core:security"))
        api(project(":feature:user"))
        api(project(":feature:settingsapi"))
        api(project(":feature:securityapi"))
    }
}