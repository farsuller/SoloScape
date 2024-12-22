plugins {
    alias (libs.plugins.android.library)
    alias (libs.plugins.kotlin.android)
    alias(libs.plugins.devtool.ksp)
    id ("io.realm.kotlin")
}

android {
    namespace = "com.soloscape.mongo"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation (libs.coroutines.core)

    implementation (libs.library.base)
    implementation (libs.realm.sync)

    //Room
    implementation(libs.bundles.bundle.room)
    ksp(libs.androidx.room.compiler)

    //util
    implementation (projects.core.util)
}