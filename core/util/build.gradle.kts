plugins {
    alias (libs.plugins.android.library)
    alias (libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id ("io.realm.kotlin")
}

android {
    namespace = "com.soloscape.util"

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
    implementation (libs.core.ktx)

    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.storage)

    implementation (libs.realm.sync)
    implementation (libs.coroutines.core)
    implementation (libs.coil)
    implementation (libs.compose.tooling.preview)

    implementation (projects.core.ui)

}