plugins {
    alias (libs.plugins.android.library)
    alias (libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias (libs.plugins.hilt)
    alias(libs.plugins.devtool.ksp)
}

android {
    namespace = "com.soloscape.dashboard"

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.bundle.androidx.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.material3)

    implementation(libs.bundles.bundle.coil)

    implementation (libs.coroutines.core)

    implementation (libs.date.time.picker)
    implementation (libs.date.dialog)
    implementation (libs.time.dialog)

    //Hilt
    implementation(libs.hilt.compose.navigation)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(platform(libs.firebase.bom))

    implementation(projects.core.ui)
    implementation(projects.core.database)
    implementation(projects.core.util)
    implementation(projects.core.messagebar)
}