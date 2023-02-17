repositories.mavenCentral()

plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("kt.coroutines.course.AppKt")
}

dependencies {
    implementation(project(":lib"))
}
