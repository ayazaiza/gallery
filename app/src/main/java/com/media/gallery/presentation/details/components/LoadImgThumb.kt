package com.media.gallery.presentation.details.components

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.media.gallery.config.AppConstants.ignoredTryCatch
import java.io.File


@Composable
fun loadImgThumb(
    url: String
): MutableState<Bitmap?> {
    val thumbnail: MutableState<Bitmap?> = remember {
        mutableStateOf(null)
    }

    fun checkUri(url: String): Uri {
        val uri = Uri.parse(url)
        return if (uri.scheme != null) {
            uri
        } else {
            Uri.fromFile(File(url))
        }
    }
    ignoredTryCatch {
        Glide.with(LocalContext.current)
            .asBitmap()
            .load(checkUri(url))
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    thumbnail.value = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })

    }

    return thumbnail
}
