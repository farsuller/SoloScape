package com.soloscape.util

import android.annotation.SuppressLint
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

import io.realm.kotlin.types.RealmInstant
import java.time.Instant

// inside function of fetchImagesFromFirebase with downloadUrl, with this a generated authentication token
//will add on image url, with that you will be enable to access the images from firebase
fun fetchImagesFromFirebase(
    remoteImagePaths : List<String>,
    onImageDownload : (Uri) -> Unit,
    onImageDownloadFailed : (Exception) -> Unit ={},
    onReadyDisplay : () -> Unit = {}
){
    if(remoteImagePaths.isNotEmpty()){
        remoteImagePaths.forEachIndexed{ index, remoteImagePath ->
            if(remoteImagePath.trim().isNotEmpty()){
                FirebaseStorage.getInstance().reference.child(remoteImagePath.trim()).downloadUrl
                    .addOnSuccessListener {
                        onImageDownload(it)
                        if(remoteImagePaths.lastIndexOf(remoteImagePaths.last()) == index){
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
fun RealmInstant.toInstant() : Instant{
    val sec : Long = this.epochSeconds
    val nano : Int = this.nanosecondsOfSecond

    return if (sec >= 0){
        Instant.ofEpochSecond(sec, nano.toLong())
    }
    else{
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