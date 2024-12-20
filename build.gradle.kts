import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias (libs.plugins.android.application) apply false
    alias (libs.plugins.android.library) apply false
    alias (libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false

    alias (libs.plugins.devtool.ksp) apply false

    alias (libs.plugins.hilt) apply false

    alias (libs.plugins.realm.kotlin) apply false

    alias (libs.plugins.gms.google.services) apply false

    alias(libs.plugins.spotless) apply false
}

buildscript {
    dependencies {
        classpath(libs.spotless)
    }
}

fun BaseExtension.defaultConfig() {
    compileSdkVersion(ProjectConfig.COMPILE_SDK)

    defaultConfig {
        minSdk = ProjectConfig.MIN_SDK
        targetSdk = ProjectConfig.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
      //  isCoreLibraryDesugaringEnabled = true
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


fun PluginContainer.applyDefaultConfig(project: Project) {

    whenPluginAdded {
        when (this) {
            is AppPlugin -> {
                project.extensions
                    .getByType<AppExtension>()
                    .apply {
                        defaultConfig()
                    }
            }
            is LibraryPlugin -> {
                project.extensions
                    .getByType<LibraryExtension>()
                    .apply {
                        defaultConfig()
                    }
            }
        }
    }
}


subprojects {
    project.plugins.applyDefaultConfig(project)
    afterEvaluate {
        project.apply("${project.rootDir}/spotless.gradle")
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
            freeCompilerArgs.addAll(
                listOf(
                    "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                    "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
                )
            )
        }
    }
}