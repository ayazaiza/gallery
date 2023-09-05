package com.media.gallery.domain.repository

import com.media.gallery.config.Resource
import com.media.gallery.domain.models.GalleryMediaItem

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MediaFileFetcherRepo {


    fun fetchAllPhotos(): Flow<Resource<List<GalleryMediaItem>>>
    fun fetchAllVideos(): Flow<Resource<List<GalleryMediaItem>>>

    fun fetchAllMedias():Flow<Resource<List<GalleryMediaItem>>>

    val queueMediaItem: List<GalleryMediaItem>

    val isMediaFetching: StateFlow<Boolean>

    suspend fun searchVideos(query: String): List<GalleryMediaItem>

    fun setQueuedVideos(list: List<GalleryMediaItem>)
    fun checkAllFolders()
    fun getVideoCard(path: String): Flow<Resource<GalleryMediaItem?>>
    fun getVideoCardById(id: String): Flow<Resource<GalleryMediaItem?>>
    fun fetchAllFiles(): Flow<Resource<List<GalleryMediaItem>>>

    fun getAllVideos(path: String?): Flow<Resource<List<GalleryMediaItem>>>

    suspend fun deleteVideoCard(path: String)

    fun cancelJob()
}