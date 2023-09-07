package com.media.gallery.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.repository.MediaFileFetcherRepo
import com.media.gallery.domain.repository.ViewModelStrResRepo
import com.media.gallery.presentation.details.models.GalleryItemDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FileDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    mediaFileFetcherRepo: MediaFileFetcherRepo,
    private val viewModelStrResRepo: ViewModelStrResRepo
) : ViewModel() {

    private val id = savedStateHandle.get<String>(AppConstants.ID_PARAM)

    private val _state = MutableStateFlow(GalleryItemDetailsState())
    val state: StateFlow<GalleryItemDetailsState> = _state.asStateFlow()

    init {
        if (id != null) {
            mediaFileFetcherRepo.getVideoCardById(id).onStart {
                _state.update {
                    it.copy(isLoading = true)
                }
            }.onEach { resource ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        galleryMediaItem = resource.data,
                        msg = resource.error
                            ?: if (resource.data == null) viewModelStrResRepo.detailsNotFound else null

                    )
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)

        }
    }
}