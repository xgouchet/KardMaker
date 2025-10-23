plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
    alias(libs.plugins.kotlinPluginSerialization)
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

dependencies {
//    implementation(project(":core"))

    implementation(libs.clikt)
    implementation(libs.kotlinxSerialization)
    implementation("com.twelvemonkeys.imageio:imageio-webp:3.12.0")
}

application {
    mainClass = "fr.xgouchet.kardmaker.AppKt"
}

tasks.jar {
    manifest.attributes("Main-Class" to application.mainClass.get())
}

tasks.shadowJar {
    manifest.attributes["Main-Class"] =  application.mainClass.get()
}
