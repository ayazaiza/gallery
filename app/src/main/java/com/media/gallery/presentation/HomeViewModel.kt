package com.media.gallery.presentation

import androidx.lifecycle.ViewModel
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.models.HomeState
import com.media.gallery.domain.models.PlayerAppThemes
import com.media.gallery.domain.repository.MediaFileFetcherRepo
import com.media.gallery.domain.repository.SharedPreferenceRepo
import com.media.gallery.domain.repository.ViewModelStrResRepo
import com.media.gallery.presentation.navigation.HomeViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaFileFetcherRepo: MediaFileFetcherRepo,
    private val sharedPreferenceRepo: SharedPreferenceRepo,
    private val viewModelStrResRepo: ViewModelStrResRepo,
) : ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    private var _job: Job? = null

    init {
        updateUi()
    }

    private fun updateUi() {
        _state.update { tmpState ->
            tmpState.copy(
                resume = sharedPreferenceRepo.resume,
                autoPlay = sharedPreferenceRepo.autoPlay,
                isDynamicTheme = sharedPreferenceRepo.dynamicTheme,
                theme = when (sharedPreferenceRepo.theme) {
                    AppConstants.THEME_DARK -> {
                        PlayerAppThemes.Dark
                    }

                    AppConstants.THEME_LIGHT -> {
                        PlayerAppThemes.Light
                    }

                    AppConstants.THEME_FOLLOW_SYSTEM -> {
                        PlayerAppThemes.SystemTheme
                    }

                    else -> {
                        PlayerAppThemes.Light
                    }
                },
                themesOption = viewModelStrResRepo.settingThemesOptions.map {
                    it.copy(
                        isSelected = sharedPreferenceRepo.theme == it.id
                    )
                },
                showThemes = false
            )
        }


    }

    fun onEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.PlayVideos -> {
                mediaFileFetcherRepo.setQueuedVideos(event.list)
            }

            is HomeViewEvent.ShowAds -> {
            }

            is HomeViewEvent.DestroyAds -> {
            }

            is HomeViewEvent.DynamicTheme -> {
                sharedPreferenceRepo.dynamicTheme = event.enable
                updateUi()
            }

            is HomeViewEvent.PlayLink -> {

            }

            is HomeViewEvent.ShowAdsFromCompose -> {

            }

            is HomeViewEvent.ChangeTheme -> {
                if (sharedPreferenceRepo.theme == event.theme) {
                    _state.update {
                        it.copy(showThemes = false)
                    }
                    return
                }
                sharedPreferenceRepo.theme = event.theme
                updateUi()
            }

            is HomeViewEvent.AutoPlay -> {
                sharedPreferenceRepo.autoPlay = event.autoPlay
                _state.update {
                    it.copy(autoPlay = sharedPreferenceRepo.autoPlay)
                }
            }

            is HomeViewEvent.Resume -> {
                sharedPreferenceRepo.resume = event.resume
                _state.update {
                    it.copy(resume = sharedPreferenceRepo.resume)
                }
            }

            is HomeViewEvent.ShowNdHideTheme -> {
                _state.update {
                    it.copy(showThemes = event.themes)
                }
            }

            is HomeViewEvent.ShowAdsSlow -> {
            }

            HomeViewEvent.ShareApp -> {}
            is HomeViewEvent.ShowRewardedAds -> {

            }

            HomeViewEvent.ShowRewardAd -> {

            }

            HomeViewEvent.RewardedAdCancel -> {
                _state.update { homeState ->
                    homeState.copy(
                        loadingDialog = false, errorDialog = false, adErrorMsg = null
                    )
                }
            }
        }
    }

    override fun onCleared() {
        _job?.cancel()
        super.onCleared()
    }

}