package com.media.gallery.domain.models


data class HomeState(
    val isDynamicTheme: Boolean = false,
    val theme: PlayerAppThemes = PlayerAppThemes.Light,
    val resume: Boolean = true,
    val autoPlay: Boolean = true,
    val showThemes: Boolean = false,
    val themesOption: List<OptionsMenu> = emptyList(),
    val loadingDialog: Boolean = false,
    val errorDialog: Boolean = false,
    val isOnePremium: Boolean = false,
    val adErrorMsg: String? = null
)

sealed interface PlayerAppThemes {
    data object SystemTheme : PlayerAppThemes
    data object Light : PlayerAppThemes
    data object Dark : PlayerAppThemes
}
