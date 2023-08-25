package com.media.gallery.data.data_source.dao.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.media.gallery.config.AppConstants
import com.media.gallery.config.AppConstants.TYPE_AUDIOS
import com.media.gallery.config.AppConstants.TYPE_VIDEOS
import com.media.gallery.domain.extensions.isWebP
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "galleryMediaItem", indices = [(Index(value = ["full_path"], unique = true))])
data class GalleryMediaItemEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "filename") var title: String = "",
    @ColumnInfo(name = "full_path") var path: String = "",
    @ColumnInfo(name = "content_uri") var uri: String = "",
    @ColumnInfo(name = "mime_type") val mimeType: String = "",
    @ColumnInfo(name = "parent_path") var parentPath: String = "",
    @ColumnInfo(name = "last_modified") var lastModified: Long = 0L,
    @ColumnInfo(name = "date_taken") var taken: Long = 0L,
    @ColumnInfo(name = "size") var size: Long = 0L,
    @ColumnInfo(name = "type") var type: Int = 0,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "deleted_ts") var deletedTS: Long = 0L,
    @ColumnInfo(name = "media_store_id") var mediaStoreId: Long = 0L,
    @ColumnInfo(name = "height") var height: Int = 0,
    @ColumnInfo(name = "width") var width: Int = 0,
    @ColumnInfo(name = "parent_name") var folderName: String = "",
    @ColumnInfo(name = "duration") var duration: Long = 0L,
    @ColumnInfo(name = "folderId") var folderId: String = "",
    @ColumnInfo(name = "lastPosition") val lastPosition: Long = 0,
    @ColumnInfo(name = "folderSize") val folderSize: Int = 0,
    @ColumnInfo(name = "timestamp") var timestamp: Long = 0L
) : Parcelable{
    fun isVideo() = type == TYPE_VIDEOS

    fun isAudio() = type == TYPE_AUDIOS

    fun isWebP() = title.isWebP()

    fun isGIF() = type == AppConstants.TYPE_GIFS

    fun isImage() = type == AppConstants.TYPE_IMAGES


    fun isRaw() = type == AppConstants.TYPE_RAW

    fun isSVG() = type == AppConstants.TYPE_SVG

    fun isPortrait() = type == AppConstants.TYPE_PORTRAITS
}

