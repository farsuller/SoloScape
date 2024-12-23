plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.devtool.ksp)
    alias (libs.plugins.hilt)
}

android {
    namespace = "com.soloscape.database"
}

dependencies {
    implementation(libs.androidx.activity.compose)

    implementation(libs.bundles.bundle.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.hilt.compose.navigation)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation (projects.core.model)
    implementation(projects.core.util)
}