package com.media.gallery.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.media.gallery.domain.models.PlayerAppThemes
import com.media.gallery.presentation.navigation.GalleryRouteHost
import com.media.gallery.ui.theme.GalleryMediaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val homeState by viewModel.state.collectAsStateWithLifecycle()
            GalleryMediaTheme(
                darkTheme = when (homeState.theme) {
                    PlayerAppThemes.SystemTheme -> isSystemInDarkTheme()
                    PlayerAppThemes.Dark -> true
                    else -> false
                }, dynamicColor = homeState.isDynamicTheme
            ) {
                val navController = rememberNavController()
                GalleryRouteHost(
                    navController = navController,
                    onEvent = viewModel::onEvent,
                    homeState = homeState
                )
            }
        }
    }
}

