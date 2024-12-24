plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

}

android {
    namespace = "com.soloscape.util"

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.bundle.androidx.compose)

    implementation(libs.material)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.animation)

    implementation(libs.androidx.compose.navigation)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)

    implementation(libs.coroutines.core)

    implementation(libs.bundles.bundle.coil)

    implementation(projects.core.ui)
}