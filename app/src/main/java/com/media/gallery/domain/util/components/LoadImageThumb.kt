package com.media.gallery.domain.util.components

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MovieCreation
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun LoadImageThumb(
    url: String,
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Outlined.MovieCreation
) {
    val context = LocalContext.current
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

    LaunchedEffect(key1 = url) {
        launch(Dispatchers.IO) {
            Glide.with(context)
                .asBitmap()
                .load(checkUri(url))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        thumbnail.value = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }

    }


    Box(modifier = modifier) {
        Icon(
            imageVector = imageVector,
            contentDescription = url,
            modifier = Modifier.align(Alignment.Center),
            tint = Color.DarkGray
        )
        if (thumbnail.value != null) {
            Image(
                bitmap = thumbnail.value!!.asImageBitmap(),
                contentDescription = url,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.1f))
        )
    }

}

