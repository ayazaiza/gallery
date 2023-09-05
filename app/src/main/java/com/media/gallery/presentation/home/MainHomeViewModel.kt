package com.media.gallery.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.media.gallery.config.AppConstants
import com.media.gallery.data.mapper.galleryAppSort
import com.media.gallery.domain.extensions.getPageViewType
import com.media.gallery.domain.repository.MediaFileFetcherRepo
import com.media.gallery.domain.repository.SharedPreferenceRepo
import com.media.gallery.domain.repository.ViewModelStrResRepo
import com.media.gallery.presentation.home.models.MainHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "MainHomeViewModel"

@HiltViewModel
class MainHomeViewModel @Inject constructor(
    private val viewModelStrResRepo: ViewModelStrResRepo,
    mediaFileFetcherRepo: MediaFileFetcherRepo,
    private val sharedPreferenceRepo: SharedPreferenceRepo
) : ViewModel() {

    private var _job: Job? = null
    private var photosSortType: Int
        get() = sharedPreferenceRepo.photosSortType
        set(value) {
            sharedPreferenceRepo.photosSortType = value
        }
    private var photosViewType: Int
        get() = sharedPreferenceRepo.photosViewType
        set(value) {
            sharedPreferenceRepo.photosViewType = value
        }

    private var allAlbumsSortType: Int
        get() = sharedPreferenceRepo.folderSortType
        set(value) {
            sharedPreferenceRepo.folderSortType = value
        }
    private var allAlbumsViewType: Int
        get() = sharedPreferenceRepo.folderViewType
        set(value) {
            sharedPreferenceRepo.folderViewType = value
        }

    private var videosSort: Int
        get() = sharedPreferenceRepo.videosSortType
        set(value) {
            sharedPreferenceRepo.videosSortType = value
        }
    private var videosViewType: Int
        get() = sharedPreferenceRepo.videosViewType
        set(value) {
            sharedPreferenceRepo.videosViewType = value
        }

    private val _state: MutableStateFlow<MainHomeState> = MutableStateFlow(MainHomeState())
    var state: StateFlow<MainHomeState> = _state.asStateFlow()
        private set

    init {
        initiateView()
        _job?.cancel()
        _job = mediaFileFetcherRepo.fetchAllMedias().onEach { resp ->
            Log.e(TAG, resp.data?.size.toString())
            val emptyMsg = resp.error
                ?: if (resp.data.isNullOrEmpty()) viewModelStrResRepo.photosNotFound else null
            val photos = resp.data?.filter { media -> media.type != AppConstants.TYPE_VIDEOS }
                ?.galleryAppSort(photosSortType) ?: emptyList()
            val albums =
                resp.data?.sortedByDescending { it.lastModified }?.distinctBy { it.parentPath }
                    ?.galleryAppSort(allAlbumsSortType)
                    ?: emptyList()
            val videos = resp.data?.filter { media -> media.type == AppConstants.TYPE_VIDEOS }
                ?.galleryAppSort(videosViewType) ?: emptyList()
            _state.update {
                it.copy(
                    photosState = it.photosState.copy(
                        mediaItems = photos,
                        emptyMsg = emptyMsg
                            ?: if (photos.isEmpty()) viewModelStrResRepo.filesNotFound else null
                    ),
                    albumsState = it.albumsState.copy(
                        albums = albums,
                        emptyMsg = emptyMsg
                    ),
                    videosState = it.videosState.copy(
                        mediaItems = videos,
                        emptyMsg = emptyMsg
                            ?: if (videos.isEmpty()) viewModelStrResRepo.videosNotFound else null
                    )
                )
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun onEvent(event: MainHomeEvent) {
        when (event) {
            is MainHomeEvent.ChangePhotoSortByOption -> {
                photosSortType = event.optionsMenu.id
                updatePhotoSort()
            }

            is MainHomeEvent.ChangePhotosViewTypeOption -> {
                photosViewType = event.optionsMenu.id
                updatePhotosViewType()
            }

            is MainHomeEvent.ChangeAlbumsSortByOption -> {
                allAlbumsSortType = event.optionsMenu.id
                updateAlbumsSort()
            }

            is MainHomeEvent.ChangeAlbumsViewTypeOption -> {
                allAlbumsViewType = event.optionsMenu.id
                updateAlbumsViewType()
            }

            is MainHomeEvent.ChangeVideoSortByOption -> {
                videosSort = event.optionsMenu.id
                updateVideosSort()
            }

            is MainHomeEvent.ChangeVideosViewTypeOption -> {
                videosViewType = event.optionsMenu.id
                updateVideosViewType()
            }

            is MainHomeEvent.ChangeCurrentRoute -> {
                _state.update { it.copy(currentRoute = event.currentRoute) }
            }
        }
    }


    private fun initiateView() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                bottomList = viewModelStrResRepo.bottomItems,
                photosState = mainHomeState.photosState.copy(
                    viewTypeOptions = viewModelStrResRepo.viewTypeOptions.map {
                        it.copy(isSelected = photosViewType == it.id)
                    },
                    sortOptions = viewModelStrResRepo.sortOptions.map { it.copy(isSelected = photosSortType == it.id) },
                    pageViewType = photosViewType.getPageViewType()
                ),
                albumsState = mainHomeState.albumsState.copy(
                    albumsSortList = viewModelStrResRepo.sortOptions.map { it.copy(isSelected = allAlbumsSortType == it.id) },
                    albumsViewType = viewModelStrResRepo.viewTypeOptions.filter { it.id != AppConstants.LARGE_LIST_VIEW }
                        .map { it.copy(isSelected = it.id == allAlbumsViewType) },
                    albumsPageViewType = allAlbumsViewType.getPageViewType(3)
                ),
                videosState = mainHomeState.videosState.copy(
                    pageViewType = videosViewType.getPageViewType(),
                    sortOptions = viewModelStrResRepo.sortOptions.map { it.copy(isSelected = it.id == videosSort) },
                    viewTypeOptions = viewModelStrResRepo.viewTypeOptions.map { it.copy(isSelected = videosViewType == it.id) }
                )
            )
        }
    }


    /* Tab-1 photos updates */
    private fun updatePhotoSort() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                photosState = mainHomeState.photosState.copy(
                    mediaItems = mainHomeState.photosState.mediaItems.galleryAppSort(
                        photosSortType
                    ),
                    sortOptions = viewModelStrResRepo.sortOptions.map { it.copy(isSelected = photosSortType == it.id) },
                )
            )
        }
    }

    private fun updatePhotosViewType() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                photosState = mainHomeState.photosState.copy(
                    viewTypeOptions = viewModelStrResRepo.viewTypeOptions.map {
                        it.copy(
                            isSelected = photosViewType == it.id
                        )
                    },
                    pageViewType = photosViewType.getPageViewType()
                )
            )
        }
    }

    /* Tab-2 albums updates */
    private fun updateAlbumsSort() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                albumsState = mainHomeState.albumsState.copy(
                    albums = mainHomeState.albumsState.albums.galleryAppSort(
                        allAlbumsSortType
                    ),
                    albumsSortList = viewModelStrResRepo.sortOptions.map { it.copy(isSelected = allAlbumsSortType == it.id) },
                )
            )
        }
    }

    private fun updateAlbumsViewType() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                albumsState = mainHomeState.albumsState.copy(
                    albumsViewType = viewModelStrResRepo.viewTypeOptions
                        .filter { it.id != AppConstants.LARGE_LIST_VIEW }
                        .map { it.copy(isSelected = it.id == allAlbumsViewType) },
                    albumsPageViewType = allAlbumsViewType.getPageViewType(3)
                )
            )
        }
    }

    /* Tab-3 videos updates */
    private fun updateVideosSort() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                videosState = mainHomeState.videosState.copy(
                    mediaItems = mainHomeState.videosState.mediaItems.galleryAppSort(
                        videosSort
                    ),
                    sortOptions = viewModelStrResRepo.sortOptions.map { it.copy(isSelected = videosSort == it.id) },
                )
            )
        }
    }

    private fun updateVideosViewType() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                videosState = mainHomeState.videosState.copy(
                    viewTypeOptions = viewModelStrResRepo.viewTypeOptions
                        .map { it.copy(isSelected = it.id == videosViewType) },
                    pageViewType = videosViewType.getPageViewType()
                )
            )
        }
    }

}