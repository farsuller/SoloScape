package com.soloscape.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.pm.PackageInfoCompat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun Long.toLocalDateOrNull(zone: ZoneId = ZoneId.systemDefault()): LocalDate? {
    return runCatching {
        Instant.ofEpochMilli(this).atZone(zone).toLocalDate()
    }.getOrNull()
}

// Convert Long to ZonedDateTime
fun Long?.toZonedDateTimeOrNull(): ZonedDateTime? =
    this?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()) }

// Convert ZonedDateTime to Long
fun ZonedDateTime?.toEpochMilliOrNull(): Long? =
    this?.toInstant()?.toEpochMilli()

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

@Composable
fun Modifier.clickableWithoutRipple(
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this.then(
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        ),
    )
}
