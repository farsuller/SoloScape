plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

}

android {
    namespace = "com.soloscape.model"
}

dependencies {
    implementation(libs.androidx.activity.compose)
}