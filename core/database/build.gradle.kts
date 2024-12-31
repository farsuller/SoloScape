plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.devtool.ksp)
    alias (libs.plugins.hilt)
}

android {
    namespace = "com.soloscape.database"

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)

    implementation(libs.bundles.bundle.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(projects.core.util)

    testImplementation (libs.truth)
    testImplementation(libs.junit)
    testImplementation (libs.androidx.core.testing)
    testImplementation (libs.kotlinx.coroutines.test)
}