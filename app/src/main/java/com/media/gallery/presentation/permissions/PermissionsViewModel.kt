package com.media.gallery.presentation.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.media.gallery.di.RequestedPermissionsArray
import com.media.gallery.domain.repository.MediaFileFetcherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PermissionsViewModel @Inject constructor(
    @RequestedPermissionsArray permissions: Array<String>,
    private val mediaFileFetcherRepo: MediaFileFetcherRepo
) : ViewModel() {

    val requestedPermissions: Array<String> = permissions

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun showAlertDialog() {
        _showDialog.update { true }
    }

    fun hideAlertDialog() {
        _showDialog.update { false }
    }

    fun fetchData() {
        viewModelScope.launch {
            mediaFileFetcherRepo.checkAllFolders()
        }
    }
}