package com.media.gallery.presentation

import androidx.lifecycle.ViewModel
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.models.HomeState
import com.media.gallery.domain.models.PlayerAppThemes
import com.media.gallery.domain.repository.MediaFileFetcherRepo
import com.media.gallery.domain.repository.SharedPreferenceRepo
import com.media.gallery.domain.repository.ViewModelStrRes
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
    private val viewModelStrRes: ViewModelStrRes,
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
                themesOption = viewModelStrRes.settingThemes.map {
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
//                adMobAdsRepo.openInterstitialAds(event.activity, event.loadNextAds, event.showNext)
            }

            is HomeViewEvent.DestroyAds -> {
//                adMobAdsRepo.releaseAds()
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

            is HomeViewEvent.ShowAdsSlow -> {/* adMobAdsRepo.openInterAdsWithoutAction(
                     event.activity,
                     event.loadNextAds,
                     event.showNext
                 )*/
            }

            HomeViewEvent.ShareApp -> {}
            is HomeViewEvent.ShowRewardedAds -> {/*  _state.update { homeState ->
                      homeState.copy(
                          loadingDialog = true,
                          errorDialog = false,
                          adErrorMsg = null
                      )
                  }
                  viewModelScope.launch {
                      delay(3000)
                      adMobAdsRepo.loadNewRewardedAds(event.activity,
                          loadingDismiss = {
                              _state.update { homeState ->
                                  homeState.copy(
                                      loadingDialog = false,
                                      errorDialog = false,
                                      adErrorMsg = null
                                  )
                              }
                          }, tryAgain = {
                              Log.e(TAG, "onEvent: ")
                              _state.update { homeState ->
                                  homeState.copy(
                                      loadingDialog = false,
                                      errorDialog = true,
                                      adErrorMsg = viewModelStrRes.somethingWentWrong
                                  )
                              }
                          }, onAdFailed = {
                              _state.update { homeState ->
                                  homeState.copy(
                                      loadingDialog = false,
                                      errorDialog = true,
                                      adErrorMsg = viewModelStrRes.videoAdsIsNotAvailable
                                  )
                              }
                          }, adImpression = {
                          }, showError = {
                              _state.update { homeState ->
                                  homeState.copy(
                                      loadingDialog = false,
                                      errorDialog = true,
                                      adErrorMsg = viewModelStrRes.failedToWatchVideo
                                  )
                              }
                          }, adDismissed = {
                              _state.update { homeState ->
                                  homeState.copy(
                                      loadingDialog = false,
                                      errorDialog = false,
                                      adErrorMsg = null
                                  )
                              }
                          })
                  }*/

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