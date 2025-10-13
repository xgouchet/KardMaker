plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
    alias(libs.plugins.kotlinPluginSerialization)
}

dependencies {
//    implementation(project(":core"))

    implementation(libs.clikt)
    implementation(libs.kotlinxSerialization)
    implementation("com.twelvemonkeys.imageio:imageio-webp:3.12.0")
}

application {
    mainClass = "fr.xgouchet.karmaker.AppKt"
}
