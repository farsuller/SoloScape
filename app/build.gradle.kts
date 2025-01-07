import java.io.FileNotFoundException
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.devtool.ksp)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.firebase.crashlytics)
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

val isGenerateBuild = ProjectConfig.GENERATE_LOCAL_ARCHIVE
val configVersionCode = ProjectConfig.VERSION_CODE
val configMajorVersion = ProjectConfig.MAJOR_VERSION
val configMinorVersion = ProjectConfig.MINOR_VERSION
val configPatchVersion = ProjectConfig.PATCH_VERSION
val appName = ProjectConfig.APP_FILE_NAME

android {
    namespace = ProjectConfig.NAMESPACE

    defaultConfig {
        applicationId = ProjectConfig.APPLICATION_ID
        versionCode = 34
        versionName = "2.1.1"

        if (isGenerateBuild) {
            versionCode = configVersionCode
            versionName = "${configMajorVersion}.${configMinorVersion}.${configPatchVersion}"

            applicationVariants.all {
                base.archivesName.set("$appName-${buildType.name}-$versionCode-$versionName")
            }
        }

        testInstrumentationRunner = "com.compose.soloscape.HiltTestRunner"
    }

    if (isGenerateBuild) {
        signingConfigs {
            register("release") {
                storeFile = file("keystore/soloscape.jks")
                storePassword = keystoreProperties["releaseStorePassword"].toString()
                keyAlias = keystoreProperties["releaseKeyAlias"].toString()
                keyPassword = keystoreProperties["releaseKeyPassword"].toString()
            }
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
            if (isGenerateBuild) signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    implementation(libs.material)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.compose.navigation)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    //Room
    implementation(libs.bundles.bundle.room)
    ksp(libs.androidx.room.compiler)

    //Hilt
    implementation(libs.hilt.compose.navigation)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // Splash API
    implementation(libs.splash.api)

    //App Updates
    implementation(libs.bundles.bundle.app.updates)

    implementation(libs.coroutines.core)

    //Profile Installer
    implementation(libs.profileinstaller)

    implementation(projects.core.ui)
    implementation(projects.core.database)
    implementation(projects.core.util)

    implementation(projects.feature.dashboard)
    implementation(projects.feature.felt)
    implementation(projects.feature.idea)

    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
}