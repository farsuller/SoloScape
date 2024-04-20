plugins {
    alias (libs.plugins.android.library)
    alias (libs.plugins.kotlin.android)
    alias (libs.plugins.hilt)
    id ("io.realm.kotlin")
    id ("com.google.devtools.ksp")
}

android {
    namespace = "com.soloscape.note"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }

    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {

    implementation (libs.activity.compose)
    implementation (libs.material3.compose)
    implementation (libs.navigation.compose)

    implementation (libs.compose.tooling.preview)

    implementation (libs.coil)

    implementation (libs.coroutines.core)

    implementation (libs.realm.sync)

    implementation (libs.date.time.picker)
    implementation (libs.date.dialog)
    implementation (libs.time.dialog)

    implementation (libs.hilt.navigation.compose)
    implementation (libs.hilt.android)

    ksp (libs.hilt.compiler)

    implementation (libs.firebase.auth)
    implementation (libs.firebase.storage)

    implementation (projects.core.ui)
    implementation (projects.core.util)
    implementation (projects.data.mongo)

    debugImplementation (libs.androidx.ui.tooling)
    debugImplementation (libs.androidx.ui.test.manifest)
}