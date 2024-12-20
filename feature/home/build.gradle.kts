plugins {
    alias (libs.plugins.android.library)
    alias (libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias (libs.plugins.hilt)
    id ("io.realm.kotlin")
    id ("com.google.devtools.ksp")
}

android {
    namespace = "com.soloscape.home"

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
    implementation (libs.activity.compose)
    implementation (libs.material3.compose)
    implementation (libs.navigation.compose)
    implementation (libs.coroutines.core)

    implementation (libs.realm.sync)

    implementation (libs.hilt.navigation.compose)
    implementation (libs.hilt.android)
    ksp (libs.hilt.compiler)

    implementation (libs.compose.tooling.preview)

    implementation (libs.date.time.picker)
    implementation (libs.date.dialog)

    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.auth)
    implementation (libs.firebase.storage)

    implementation (projects.core.ui)
    implementation (projects.core.util)
    implementation (projects.data.mongo)

    debugImplementation (libs.androidx.ui.tooling)
    debugImplementation (libs.androidx.ui.test.manifest)

}