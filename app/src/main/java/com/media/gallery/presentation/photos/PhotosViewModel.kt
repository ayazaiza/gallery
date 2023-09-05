package com.media.gallery.presentation.photos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.media.gallery.domain.repository.MediaFileFetcherRepo
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

private const val TAG = "PhotosViewModel"

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val mediaFileFetcherRepo: MediaFileFetcherRepo
) : ViewModel() {
    private val _state = MutableStateFlow(GalleryItemsState())
    val state: StateFlow<GalleryItemsState> by lazy {
        _state.asStateFlow()
    }
    private var _job: Job? = null

    init {
        _job?.cancel()
        _job = mediaFileFetcherRepo.fetchAllPhotos().onEach { resp ->
            Log.e(TAG, resp.data?.size.toString())
            _state.update {
                it.copy(mediaItems = resp.data?.sortedByDescending { item -> item.lastModified }
                    ?: emptyList())
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }
}