import java.io.FileNotFoundException
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.devtool.ksp)
    alias (libs.plugins.gms.google.services)
}

val keystoreProperties: Properties by lazy {
    val properties = Properties()
    val keystorePropertiesFile = rootProject.file("keystore.properties")

    if (keystorePropertiesFile.exists()) {
        properties.load(keystorePropertiesFile.inputStream())
    } else {
        throw FileNotFoundException("Keystore properties file not found.")
    }
    properties
}

android {
    namespace = ProjectConfig.NAMESPACE

    defaultConfig {
        applicationId = ProjectConfig.APPLICATION_ID
        versionCode = ProjectConfig.VERSION_CODE
        versionName = "${ProjectConfig.MAJOR_VERSION}.${ProjectConfig.MINOR_VERSION}.${ProjectConfig.PATCH_VERSION}"
    }

    applicationVariants.all {
        base.archivesName.set("${ProjectConfig.APP_FILE_NAME}-${buildType.name}-$versionCode-$versionName")
    }

    signingConfigs {
        register("release") {
            storeFile = file("keystore/soloscape.jks")
            storePassword = keystoreProperties["releaseStorePassword"].toString()
            keyAlias = keystoreProperties["releaseKeyAlias"].toString()
            keyPassword = keystoreProperties["releaseKeyPassword"].toString()
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            isMinifyEnabled = false
        }

        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
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

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.storage)

    //Room
    implementation(libs.bundles.bundle.room)
    ksp(libs.androidx.room.compiler)

    //Hilt
    implementation(libs.androidx.hilt.compose.navigation)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // Splash API
    implementation (libs.splash.api)

    implementation (libs.coroutines.core)

    //Profile Installer
    implementation (libs.profileinstaller)

    implementation (projects.core.ui)
    implementation (projects.core.model)
    implementation (projects.core.database)
    implementation (projects.core.util)

    implementation (projects.feature.dashboard)
    implementation (projects.feature.home)
    //implementation (projects.feature.note)

}