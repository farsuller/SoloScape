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


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation(libs.activity.compose)
    implementation(libs.material3.compose)

    implementation(libs.compose.tooling.preview)

    implementation(libs.navigation.compose)

    implementation(libs.message.bar.compose)
    implementation(libs.one.tap.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(libs.coroutines.core)

    implementation(libs.realm.sync)

    implementation(projects.core.ui)
    implementation(projects.core.util)

    debugImplementation (libs.androidx.ui.tooling)
    debugImplementation (libs.androidx.ui.test.manifest)
}