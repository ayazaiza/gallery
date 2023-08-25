package com.media.gallery.domain.models

data class GalleryMediaItem(
    val id: Int? = null,
    val title: String = "",
    val path: String = "",
    val uri: String = "",
    val mimeType: String = "",
    val parentPath: String = "",
    val lastModified: Long = 0L,
    val taken: Long = 0L,
    val size: Long = 0L,
    val type: Int = 0,
    val isFavorite: Boolean = false,
    val deletedTS: Long = 0L,
    val mediaStoreId: Long = 0L,
    val height: Int = 0,
    val width: Int = 0,
    val folderName: String = "",
    val duration: Long = 0L,
    val folderId: String = "",
    val lastPosition: Long = 0,
    val folderSize: Int = 0,
    val timestamp: Long = 0L,
    val mediaCount: Int = 0,
    val isFile: Boolean = true
)

//class InvalidateVideoCardException(message: String) : Exception(message)

