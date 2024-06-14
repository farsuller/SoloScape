plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.soloscape.ui"

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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation (libs.activity.compose)
    implementation (libs.material3.compose)
    implementation (libs.compose.tooling.preview)
    implementation (libs.realm.sync)
    implementation (libs.coroutines.core)
    implementation (libs.material.icons.extended)

    debugImplementation (libs.androidx.ui.tooling)
    debugImplementation (libs.androidx.ui.test.manifest)
}