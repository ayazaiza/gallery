package com.media.gallery.presentation.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.media.gallery.config.AppConstants
import com.media.gallery.data.mapper.galleryAppSort
import com.media.gallery.domain.extensions.getPageViewType
import com.media.gallery.domain.repository.MediaFileFetcherRepo
import com.media.gallery.domain.repository.SharedPreferenceRepo
import com.media.gallery.domain.repository.ViewModelStrResRepo
import com.media.gallery.presentation.home.MainHomeEvent
import com.media.gallery.presentation.photos.models.GalleryItemsState
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

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val viewModelStrResRepo: ViewModelStrResRepo,
    mediaFileFetcherRepo: MediaFileFetcherRepo,
    private val sharedPreferenceRepo: SharedPreferenceRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _job: Job? = null
    private val path = savedStateHandle.get<String>(AppConstants.PATH_PARAM)
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

    private val _state: MutableStateFlow<GalleryItemsState> = MutableStateFlow(GalleryItemsState())
    var state: StateFlow<GalleryItemsState> = _state.asStateFlow()
        private set

    init {
        initiateView()
        _job?.cancel()
        _job = mediaFileFetcherRepo.getAllVideos(path).onEach { resp ->
            val emptyMsg = resp.error
                ?: if (resp.data.isNullOrEmpty()) viewModelStrResRepo.filesNotFound else null
            val items = resp.data
                ?.galleryAppSort(photosSortType) ?: emptyList()

            _state.update {
                it.copy(
                    mediaItems = items,
                    emptyMsg = emptyMsg
                )
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun onEvent(event: MainHomeEvent) {
        when (event) {
            is MainHomeEvent.ChangePhotoSortByOption -> {
                photosSortType = event.optionsMenu.id
                updateSort()
            }

            is MainHomeEvent.ChangePhotosViewTypeOption -> {
                photosViewType = event.optionsMenu.id
                updateViewType()
            }

            else -> {}
        }
    }


    private fun initiateView() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                viewTypeOptions = viewModelStrResRepo.viewTypeOptions.map {
                    it.copy(isSelected = photosViewType == it.id)
                },
                sortOptions = viewModelStrResRepo.sortOptions.map { it.copy(isSelected = photosSortType == it.id) },
                pageViewType = photosViewType.getPageViewType()

            )
        }
    }


    private fun updateSort() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                mediaItems = mainHomeState.mediaItems.galleryAppSort(
                    photosSortType
                ),
                sortOptions = viewModelStrResRepo.sortOptions.map { it.copy(isSelected = photosSortType == it.id) },
            )
        }
    }

    private fun updateViewType() {
        _state.update { mainHomeState ->
            mainHomeState.copy(
                viewTypeOptions = viewModelStrResRepo.viewTypeOptions.map {
                    it.copy(
                        isSelected = photosViewType == it.id
                    )
                },
                pageViewType = photosViewType.getPageViewType()
            )
        }
    }

}