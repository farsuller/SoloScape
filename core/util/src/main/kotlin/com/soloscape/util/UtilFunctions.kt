package com.soloscape.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.pm.PackageInfoCompat
import coil3.ImageLoader
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun provideImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(AnimatedImageDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
}

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

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f,
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale,
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f,
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90,
        ),
        targetScale = targetScale,
    ) + fadeOut(tween(delayMillis = 90))
}

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
