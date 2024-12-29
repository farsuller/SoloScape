import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.addAll(
                listOf(
                    "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                    "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                    "-opt-in=androidx.compose.animation.ExperimentalAnimationApi"

                )
            )
        }
    }
}