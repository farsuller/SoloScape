plugins {
    alias (libs.plugins.android.library)
    alias (libs.plugins.kotlin.android)
    id ("io.realm.kotlin")
}

android {
    namespace = "com.soloscape.util"

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
    implementation (libs.core.ktx)

    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.storage)

    implementation (libs.realm.sync)
    implementation (libs.coroutines.core)
    implementation (libs.coil)
    implementation (libs.compose.tooling.preview)

    implementation (projects.core.ui)

}