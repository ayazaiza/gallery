package com.media.gallery.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.media.gallery.data.data_source.dao.GalleryItemDao
import com.media.gallery.data.data_source.dao.entities.GalleryMediaItemEntity

@Database(
    entities = [GalleryMediaItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PlayerRoomDatabase : RoomDatabase() {
    abstract val videoCardsDao: GalleryItemDao

    companion object {
        const val DATABASE_NAME = "the_gallery_db"
    }
}