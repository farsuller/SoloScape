// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias (libs.plugins.android.application) apply false
    alias (libs.plugins.android.library) apply false
    alias (libs.plugins.android.test) apply false
    alias (libs.plugins.kotlin.android) apply false
    alias (libs.plugins.hilt) apply false
    alias (libs.plugins.realm.kotlin) apply false
    alias (libs.plugins.gms.google.services) apply false
    alias (libs.plugins.devtool.ksp) apply false
}