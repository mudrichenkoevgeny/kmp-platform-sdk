plugins {
    `java-platform`
}

dependencies {
    constraints {
        api(project(":core:common"))
        api(project(":feature:user"))
    }
}