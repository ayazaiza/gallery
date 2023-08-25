package com.media.gallery.domain.util.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.media.gallery.R
import com.media.gallery.ui.theme.promptFamily

@Composable
fun CustomNativeCompAdView(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        Column(
            Modifier.padding(8.dp)
        ) {
            Row {
                Box(
                    Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Image(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Rounded.MusicNote,
                        contentDescription = stringResource(
                            id = R.string.music_player_title
                        ),
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.primary
                        )
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(
                    verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.music_text_desc),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = promptFamily
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row {
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.primary
                                )
                        ) {
                            Text(
                                text = "AD", modifier = Modifier
                                    .align(
                                        Alignment.Center
                                    )
                                    .padding(
                                        horizontal = 4.dp, vertical = 1.dp
                                    ),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = promptFamily,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(id = R.string.music_text_desc),
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = promptFamily,
                            ),
                            maxLines = 1,
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)

            ) {
                Image(
                    modifier = Modifier
                        .size(160.dp)
                        .padding(20.dp)
                        .align(Alignment.Center),
                    imageVector = Icons.Rounded.MusicNote,
                    contentDescription = stringResource(id = R.string.music_text_desc),
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Text(
                    text = stringResource(id = R.string.music_text_desc),
                    modifier = Modifier.weight(7f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = promptFamily
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    Modifier
                        .weight(3f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            MaterialTheme.colorScheme.primary
                        )
                        .clickable {
                            onClick.invoke()
                        }) {
                    Text(
                        text = stringResource(id = R.string.install), modifier = Modifier
                            .align(
                                Alignment.Center
                            )
                            .padding(
                                horizontal = 6.dp, vertical = 10.dp
                            ), style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontFamily = promptFamily
                        )
                    )
                }
            }
        }
    }
}

/*Box(
                                               modifier = Modifier
                                                   .fillMaxWidth()
                                                   .padding(8.dp)
                                                   .background(color = colorResource(id = R.color.aqua))
                                           ) {
                                               ConstraintLayout(
                                                   Modifier
                                                       .fillMaxSize()

                                               ) {
                                                   val (smallIcon, largeIcon, title, subtitle, adText,desc) = createRefs()
                                                   Box(
                                                       Modifier
                                                           .size(42.dp)
                                                           .clip(RoundedCornerShape(6.dp))
                                                           .background(color = colorResource(id = R.color.folderColor))
                                                           .constrainAs(smallIcon) {
                                                               top.linkTo(parent.top)
                                                               start.linkTo(parent.start)
                                                               bottom.linkTo(parent.bottom)
                                                           }) {
                                                       Image(
                                                           modifier = Modifier
                                                               .size(28.dp)
                                                               .align(Alignment.Center),
                                                           painter = painterResource(id = R.drawable.round_music_note_24),
                                                           contentDescription = stringResource(id = R.string.block_ads),
                                                           colorFilter = ColorFilter.tint(
                                                               colorResource(id = R.color.white)
                                                           )
                                                       )
                                                   }
                                                   Text(
                                                       text = stringResource(id = R.string.block_ads),
                                                       modifier =
                                                       Modifier.constrainAs(title) {
                                                           start.linkTo(smallIcon.end, 6.dp)
                                                           top.linkTo(smallIcon.top)
                                                           bottom.linkTo(subtitle.top)
                                                           end.linkTo(parent.end)
                                                           width = Dimension.fillToConstraints
                                                       },
                                                       style = MaterialTheme.typography.caption.copy(
                                                           fontWeight = FontWeight.Bold
                                                       ),
                                                       color = colorResource(id = R.color.text_primary)
                                                   )
                                                   Box(
                                                       Modifier
                                                           .clip(RoundedCornerShape(2.dp))
                                                           .background(color = colorResource(id = R.color.folderColor))
                                                           .constrainAs(adText) {
                                                               top.linkTo(title.bottom)
                                                               start.linkTo(title.start)
                                                               end.linkTo(subtitle.start)
                                                               bottom.linkTo(smallIcon.bottom)
                                                           }) {
                                                       Text(
                                                           text = "AD", modifier = Modifier
                                                               .align(
                                                                   Alignment.Center
                                                               )
                                                               .padding(
                                                                   horizontal = 4.dp,
                                                                   vertical = 1.dp
                                                               ),
                                                           style = MaterialTheme.typography.caption.copy(
                                                               color = colorResource(id = R.color.white),
                                                               fontSize = 10.sp,
                                                               fontWeight = FontWeight.Bold
                                                           )
                                                       )
                                                   }
                                                   Text(
                                                       text = stringResource(id = R.string.watch_short_video_free_one),
                                                       modifier =
                                                       Modifier.constrainAs(subtitle) {
                                                           start.linkTo(adText.end, 5.dp)
                                                           top.linkTo(adText.top)
                                                           bottom.linkTo(adText.bottom)
                                                           end.linkTo(parent.end)
                                                           width = Dimension.fillToConstraints
                                                       },
                                                       style = MaterialTheme.typography.caption,
                                                       overflow = TextOverflow.Ellipsis,
                                                       maxLines = 1,
                                                       color = colorResource(id = R.color.text_primary)
                                                   )
                                                   Box(modifier = Modifier
                                                       .background(color = colorResource(id = R.color.folderColor))
                                                       .constrainAs(largeIcon) {
                                                           top.linkTo(smallIcon.bottom, 10.dp)
                                                           start.linkTo(smallIcon.start)
                                                           end.linkTo(parent.end)
                                                           bottom.linkTo(desc.top)
                                                           width = Dimension.fillToConstraints
//                                                            height = Dimension.value(120.dp)
                                                       }
                                                   ) {
                                                       Image(
                                                           modifier = Modifier
                                                               .size(120.dp)
                                                               .align(Alignment.Center),
                                                           painter = painterResource(id = R.drawable.round_music_note_24),
                                                           contentDescription = stringResource(id = R.string.block_ads),
                                                           colorFilter = ColorFilter.tint(
                                                               colorResource(id = R.color.white)
                                                           )
                                                       )
                                                   }
                                                   Text(
                                                       text = stringResource(id = R.string.watch_short_video_free_one),
                                                       modifier =
                                                       Modifier.constrainAs(desc) {
                                                           start.linkTo(largeIcon.start)
                                                           top.linkTo(largeIcon.bottom)
                                                           bottom.linkTo(parent.bottom)
                                                           end.linkTo(parent.end)
                                                           width = Dimension.fillToConstraints
                                                       },
                                                       style = MaterialTheme.typography.caption,
                                                       overflow = TextOverflow.Ellipsis,
                                                       maxLines = 1,
                                                       color = colorResource(id = R.color.text_primary)
                                                   )
                                               }
                                           }*/