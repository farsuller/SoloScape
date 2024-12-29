plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.devtool.ksp)
}

android {
    namespace = "com.soloscape.felt"

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

    implementation(libs.material.icons.extended)

    implementation(libs.coroutines.core)

    implementation(libs.bundles.bundle.coil)

    //Hilt
    implementation(libs.androidx.hilt.compose.navigation)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)


    implementation(libs.date.time.picker)
    implementation(libs.date.dialog)
    implementation(libs.time.dialog)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.database)
    implementation(projects.core.util)
    implementation(projects.core.messagebar)

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation (libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

}