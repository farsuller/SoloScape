package com.soloscape.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.core.content.pm.PackageInfoCompat

fun getAppVersion(context: Context): String {
    return try {
        val packageManager: PackageManager = context.packageManager
        val packageInfo: PackageInfo = packageManager.getPackageInfo(context.packageName, 0)
        val versionCode: Long = PackageInfoCompat.getLongVersionCode(packageInfo)

        val versionName: String = packageInfo.versionName.toString()
        "v$versionName"
    } catch (e: PackageManager.NameNotFoundException) {
        "version N/A"
    }
}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.clickableWithoutRipple(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
) = composed(
    factory = {
        this.then(
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick() },
            ),
        )
    },
)
