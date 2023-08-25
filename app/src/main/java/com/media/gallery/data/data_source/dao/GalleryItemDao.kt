package com.media.gallery.data.data_source.dao

import androidx.room.*
import com.media.gallery.data.data_source.dao.entities.GalleryMediaItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryItemDao {

    @Query("SELECT * FROM galleryMediaItem")
    fun getAllMediaFiles(): List<GalleryMediaItemEntity>

    @Query("SELECT * FROM galleryMediaItem")
    fun getAllMediaFilesFlow(): Flow<List<GalleryMediaItemEntity>>

    @Query("SELECT * FROM galleryMediaItem WHERE full_path= :path COLLATE NOCASE")
    fun getMediaFileFromPathFlow(path: String): Flow<List<GalleryMediaItemEntity>>

    @Query("SELECT * FROM galleryMediaItem WHERE full_path= :path COLLATE NOCASE")
    fun getMediaFileFromPath(path: String): List<GalleryMediaItemEntity>

    @Query("SELECT * FROM galleryMediaItem WHERE id=:id COLLATE NOCASE")
    fun getMediaFileFromId(id: String): List<GalleryMediaItemEntity>

    @Query("SELECT * FROM galleryMediaItem WHERE id=:id COLLATE NOCASE")
    fun getMediaFileFromIdByFlow(id: String): Flow<List<GalleryMediaItemEntity>>

    @Query("SELECT * FROM galleryMediaItem WHERE deleted_ts = 0 AND parent_path = :path COLLATE NOCASE")
    fun getMediaFromPath(path: String): List<GalleryMediaItemEntity>

    @Query("SELECT * FROM galleryMediaItem WHERE deleted_ts = 0 AND parent_path = :path COLLATE NOCASE")
    fun getMediaFromPathFlow(path: String): Flow<List<GalleryMediaItemEntity>>


    @Query("SELECT * FROM galleryMediaItem WHERE deleted_ts != 0")
    fun getDeletedMedia(): Flow<List<GalleryMediaItemEntity>>

    @Query("SELECT COUNT(filename) FROM galleryMediaItem WHERE deleted_ts != 0")
    fun getDeletedMediaCount(): Long

    @Query("SELECT * FROM galleryMediaItem WHERE deleted_ts < :timestamp AND deleted_ts != 0")
    fun getOldRecycleBinItems(timestamp: Long): Flow<List<GalleryMediaItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(galleryMediaItemEntity: GalleryMediaItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videoCardEntity: List<GalleryMediaItemEntity>)

    @Delete
    suspend fun deleteMedia(vararg galleryMediaItemEntity: GalleryMediaItemEntity)

    @Query("DELETE FROM galleryMediaItem WHERE parent_path = :parentPath COLLATE NOCASE")
    suspend fun deleteMediaFileByParentPath(parentPath: String)

    @Query("DELETE FROM galleryMediaItem WHERE full_path = :path COLLATE NOCASE")
    suspend fun deleteMediumPath(path: String)

    @Query("UPDATE OR REPLACE galleryMediaItem SET filename = :newFilename, full_path = :newFullPath, parent_path = :newParentPath WHERE full_path = :oldPath COLLATE NOCASE")
    suspend fun updateMedium(
        oldPath: String,
        newParentPath: String,
        newFilename: String,
        newFullPath: String
    )

    @Query("UPDATE OR REPLACE galleryMediaItem SET full_path = :newPath, deleted_ts = :deletedTS WHERE full_path = :oldPath COLLATE NOCASE")
    suspend fun updateDeleted(newPath: String, deletedTS: Long, oldPath: String)


    @Query("DELETE FROM galleryMediaItem WHERE deleted_ts != 0")
    suspend fun clearRecycleBin()


    /* favorite */
    @Query("SELECT * FROM galleryMediaItem WHERE deleted_ts = 0 AND is_favorite = 1")
    fun getFavorites(): List<GalleryMediaItemEntity>


    @Query("SELECT COUNT(filename) FROM galleryMediaItem WHERE deleted_ts = 0 AND is_favorite = 1")
    fun getFavoritesCount(): Long

    @Query("SELECT COUNT(parent_path) FROM galleryMediaItem WHERE parent_path =:parentPath")
    fun getFoldersCount(parentPath: String): Long

    @Query("UPDATE galleryMediaItem SET date_taken = :dateTaken WHERE full_path = :path COLLATE NOCASE")
    suspend fun updateFavoriteDateTaken(path: String, dateTaken: Long)

    @Query("UPDATE galleryMediaItem SET is_favorite = :isFavorite WHERE full_path = :path COLLATE NOCASE")
    suspend fun updateFavorite(path: String, isFavorite: Boolean)

    @Query("UPDATE galleryMediaItem SET is_favorite = 0")
    suspend fun clearFavorites()

    @Query("SELECT * FROM galleryMediaItem WHERE full_path=:path AND deleted_ts = 0 AND is_favorite = 1")
    suspend fun getFavorite(path: String): GalleryMediaItemEntity

    @Query("SELECT * FROM galleryMediaItem WHERE filename LIKE '%' || :query || '%' OR parent_name LIKE '%' || :query || '%' OR full_path LIKE '%' || :query || '%' OR parent_path LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<GalleryMediaItemEntity>

}


/*  @Query("SELECT EXISTS(SELECT * FROM mediaVideoCard WHERE id=:id)")
  suspend fun favoriteCheckExistById(id: Int): Boolean

  @Query("SELECT EXISTS(SELECT * FROM mediaVideoCard WHERE media_store_id=:media_store_id)")
  suspend fun favoriteCheckExistByMediaStoreId(media_store_id: Long): Boolean

  @Query("SELECT EXISTS(SELECT * FROM mediaVideoCard WHERE content_uri=:uri)")
  suspend fun favoriteCheckExistByUri(uri: String): Boolean*/