package com.media.gallery.data.mapper

import com.media.gallery.config.AppConstants
import com.media.gallery.config.AppConstants.TYPE_AUDIOS
import com.media.gallery.config.AppConstants.TYPE_GIFS
import com.media.gallery.config.AppConstants.TYPE_IMAGES
import com.media.gallery.config.AppConstants.TYPE_PORTRAITS
import com.media.gallery.config.AppConstants.TYPE_RAW
import com.media.gallery.config.AppConstants.TYPE_SVG
import com.media.gallery.config.AppConstants.TYPE_VIDEOS
import com.media.gallery.data.data_source.dao.entities.GalleryMediaItemEntity
import com.media.gallery.domain.models.GalleryMediaItem

fun GalleryMediaItemEntity.toGalleryMediaItem(): GalleryMediaItem {
    return GalleryMediaItem(
        id = id,
        title = title,
        path = path,
        uri = uri,
        mimeType = mimeType,
        parentPath = parentPath,
        lastModified = lastModified,
        taken = taken,
        size = size,
        type = type,
        isFavorite = isFavorite,
        deletedTS = deletedTS,
        mediaStoreId = mediaStoreId,
        folderName = folderName,
        folderId = folderId,
        folderSize = folderSize,
        height = height,
        width = width,
        duration = duration,
        lastPosition = lastPosition,
        timestamp = timestamp
    )
}

fun GalleryMediaItem.toGalleryMediaItemEntity(): GalleryMediaItemEntity {
    return GalleryMediaItemEntity(
        id = id,
        title = title,
        path = path,
        uri = uri,
        mimeType = mimeType,
        parentPath = parentPath,
        lastModified = lastModified,
        taken = taken,
        size = size,
        type = type,
        isFavorite = isFavorite,
        deletedTS = deletedTS,
        mediaStoreId = mediaStoreId,
        folderName = folderName,
        folderId = folderId,
        folderSize = folderSize,
        height = height,
        width = width,
        duration = duration,
        lastPosition = lastPosition,
        timestamp = timestamp
    )
}

fun List<GalleryMediaItem>.galleryAppSort(sortId: Int): List<GalleryMediaItem> {
    return when (sortId) {
        AppConstants.SORT_TYPE_BY_SIZE -> sortedBy { it.size }
        AppConstants.SORT_TYPE_BY_NAME_A_TO_Z -> sortedBy { it.title }
        AppConstants.SORT_TYPE_BY_NAME_Z_TO_A -> sortedByDescending { it.title }
        AppConstants.SORT_TYPE_BY_LAST_MODIFIED -> sortedByDescending { it.lastModified }
        else -> this
    }
}

fun List<GalleryMediaItemEntity>.getDirMediaTypes(): Int {
    var types = 0
    if (any { it.isImage() }) {
        types += TYPE_IMAGES
    }

    if (any { it.isVideo() }) {
        types += TYPE_VIDEOS
    }

    if (any { it.isGIF() }) {
        types += TYPE_GIFS
    }

    if (any { it.isRaw() }) {
        types += TYPE_RAW
    }

    if (any { it.isSVG() }) {
        types += TYPE_SVG
    }

    if (any { it.isPortrait() }) {
        types += TYPE_PORTRAITS
    }

    if (any { it.isAudio() }) {
        types += TYPE_AUDIOS
    }

    return types
}