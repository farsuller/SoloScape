plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.soloscape.auth"

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

    implementation(libs.androidx.compose.navigation)

    implementation(libs.message.bar.compose)
    implementation(libs.one.tap.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation (libs.coroutines.core)

    implementation(projects.core.ui)
    implementation (projects.core.model)
    implementation(projects.core.util)

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}