package com.media.gallery.presentation.permissions

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.media.gallery.R
import com.media.gallery.domain.extensions.hasPermissions
import com.media.gallery.domain.extensions.toast
import com.media.gallery.ui.theme.promptFamily

@Composable
fun PermissionScreen(
    navToHome: () -> Unit,
    permissions: Array<String>,
    hideDialog: () -> Unit,
    showDialog: () -> Unit,
    isDialogShow: Boolean,
    intent: Intent,
    fetchData: () -> Unit
) {

    val context = LocalContext.current
    val permissionsGranted = stringResource(id = R.string.granted_permissions)
    val settingsActivity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (context.hasPermissions(permissions = permissions)) {
                context.toast(permissionsGranted)
                return@rememberLauncherForActivityResult
            }
            showDialog()
        })
    val results =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { allPerms ->
            val granted = allPerms.entries.all {
                it.value
            }
            if (granted) {
                fetchData.invoke()
                navToHome()
                return@rememberLauncherForActivityResult
            }
            showDialog()
        }

    Scaffold {
        if (isDialogShow) {
            AlertDialog(
                title = {
                    Text(
                        text = stringResource(id = R.string.attention_required),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = promptFamily
                        ),
                    )
                },
                dismissButton = {
                    TextButton(onClick = { hideDialog.invoke() }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = promptFamily
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        settingsActivity.launch(intent)
                    }) {
                        Text(
                            text = stringResource(id = R.string.settings),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = promptFamily
                            )
                        )
                    }
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.perm_text),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = promptFamily
                        )
                    )
                },
                onDismissRequest = {
                    hideDialog()
                })
        }

        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp)
            ) {
                Image(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = stringResource(id = R.string.app_name),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.size(96.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.allow_txt),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = promptFamily
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {
                    results.launch(permissions)
                }) {
                    Text(
                        text = stringResource(id = R.string.allow_permission),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}