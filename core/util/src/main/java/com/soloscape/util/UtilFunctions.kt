package com.soloscape.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.core.content.pm.PackageInfoCompat
import com.google.firebase.storage.FirebaseStorage
import io.realm.kotlin.types.RealmInstant
import java.time.Instant

// inside function of fetchImagesFromFirebase with downloadUrl, with this a generated authentication token
// will add on image url, with that you will be enable to access the images from firebase
fun fetchImagesFromFirebase(
    remoteImagePaths: List<String>,
    onImageDownload: (Uri) -> Unit,
    onImageDownloadFailed: (Exception) -> Unit = {},
    onReadyDisplay: () -> Unit = {},
) {
    if (remoteImagePaths.isNotEmpty()) {
        remoteImagePaths.forEachIndexed { index, remoteImagePath ->
            if (remoteImagePath.trim().isNotEmpty()) {
                FirebaseStorage.getInstance().reference.child(remoteImagePath.trim()).downloadUrl
                    .addOnSuccessListener {
                        onImageDownload(it)
                        if (remoteImagePaths.lastIndexOf(remoteImagePaths.last()) == index) {
                            onReadyDisplay()
                        }
                    }.addOnFailureListener {
                        onImageDownloadFailed(it)
                    }
            }
        }
    }
}

@SuppressLint("NewApi")
fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond

    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}

@SuppressLint("NewApi")
fun Instant.toRealmInstant(): RealmInstant {
    val sec: Long = this.epochSecond
    val nano: Int = this.nano
    return if (sec >= 0) {
        RealmInstant.from(sec, nano)
    } else {
        RealmInstant.from(sec + 1, -1_000_000 + nano)
    }
}

fun getAppVersion(context: Context): String {
    return try {
        val packageManager: PackageManager = context.packageManager
        val packageInfo: PackageInfo = packageManager.getPackageInfo(context.packageName, 0)
        val versionCode: Long = PackageInfoCompat.getLongVersionCode(packageInfo)

        val versionName: String = packageInfo.versionName
        "v$versionName"
    } catch (e: PackageManager.NameNotFoundException) {
        "version N/A"
    }
}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.clickableWithoutRipple(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit
) = composed(
    factory = {
        this.then(
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick() }
            )
        )
    }
)
