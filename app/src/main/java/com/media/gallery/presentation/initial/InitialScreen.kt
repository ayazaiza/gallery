package com.media.gallery.presentation.initial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.media.gallery.R
import com.media.gallery.domain.extensions.hasPermissions
import com.media.gallery.ui.theme.promptFamily
import kotlinx.coroutines.delay

@Composable
fun InitialScreen(
    title: String = stringResource(id = R.string.app_name),
    navToHome: () -> Unit,
    navToPermissions: () -> Unit,
    permissions: Array<String>,
    fetchVideo: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        if (context.hasPermissions(permissions)) {
            fetchVideo()
            delay(2000)
            navToHome.invoke()
        } else {
            navToPermissions()
        }
    })

    Scaffold {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)

        ) {
            val (logo, appName, loader) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.the_splash),
                contentDescription = title,
                modifier = Modifier.constrainAs(logo) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
            )
            Text(
                text = title,
                modifier = Modifier.constrainAs(appName) {
                    start.linkTo(logo.start)
                    end.linkTo(logo.end)
                    top.linkTo(logo.bottom)
                    bottom.linkTo(loader.top)
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = promptFamily,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.constrainAs(loader) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(appName.bottom)
                    bottom.linkTo(parent.bottom)
                })
        }
        /*Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.the_splash),
                contentDescription = title,
//                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
//                modifier = Modifier.size(96.dp)
            )
        }*/
    }
}