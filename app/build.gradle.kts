import java.io.FileNotFoundException
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    id("com.google.devtools.ksp")
    id ("io.realm.kotlin")
    id ("com.google.gms.google-services")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

dependencies {
    // Compose Navigation
    implementation (libs.navigation.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.storage)

    // Room components
    implementation (libs.room.runtime)
    ksp (libs.room.compiler)
    implementation (libs.room.ktx)

    // Splash API
    implementation (libs.splash.api)

    // Dagger Hilt
    implementation (libs.hilt.android)
    ksp (libs.hilt.compiler)

    // Mongo DB Realm
    implementation (libs.realm.sync)

    // Desugar JDK
    coreLibraryDesugaring (libs.desugar.jdk)

//    //Leak Canary
    debugImplementation (libs.leakcanary.android)

    //Profile Installer
    implementation (libs.profileinstaller)

    implementation (projects.core.ui)
    implementation (projects.core.util)
    implementation (projects.data.mongo)
    implementation (projects.feature.auth)
    implementation (projects.feature.home)
    implementation (projects.feature.note)

    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.androidx.espresso.core)

    androidTestImplementation (libs.hilt.android.testing)

    androidTestImplementation (libs.androidx.ui.test.junit4)
    debugImplementation (libs.androidx.ui.tooling)
    debugImplementation (libs.androidx.ui.test.manifest)
}