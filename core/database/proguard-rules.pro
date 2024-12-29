# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.soloscape.database.domain.model.** { *; }
-keep class com.soloscape.database.data.** { *; }

# Room library
# Preserve Room's generated classes and interfaces
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }

# Preserve @Database annotated classes
-keep @androidx.room.Database class * {
    *;
}

# Preserve @Entity annotated classes
-keep @androidx.room.Entity class * {
    *;
}

# Preserve @Dao annotated interfaces
-keep @androidx.room.Dao class * {
    *;
}