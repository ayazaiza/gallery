package com.media.gallery.presentation.initial

import androidx.lifecycle.ViewModel
import com.media.gallery.di.RequestedPermissionsArray
import com.media.gallery.domain.repository.MediaFileFetcherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class InitialScreenViewModel @Inject constructor(
    @RequestedPermissionsArray permissions: Array<String>,
    private val mediaFileFetcherRepo: MediaFileFetcherRepo
) : ViewModel() {

    val requestedPermissions: Array<String> = permissions

    fun fetchVideos() {
        mediaFileFetcherRepo.checkAllFolders()
    }
}