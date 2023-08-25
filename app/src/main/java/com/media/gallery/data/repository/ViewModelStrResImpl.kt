package com.media.gallery.data.repository

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Title
import com.media.gallery.R
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.extensions.toFirstCap
import com.media.gallery.domain.models.PlayerOption
import com.media.gallery.domain.repository.ViewModelStrRes


class ViewModelStrResImpl(
    private val context: Context
) : ViewModelStrRes {

    override val sortBy: List<PlayerOption> = listOf(
        PlayerOption(
            title = context.getString(R.string.sort_option_az).toFirstCap(),
            id = AppConstants.SORT_TYPE_BY_NAME_A_TO_Z,
            icon = Icons.Rounded.Title
        ),
        PlayerOption(
            title = context.getString(R.string.sort_option_za).toFirstCap(),
            id = AppConstants.SORT_TYPE_BY_NAME_Z_TO_A,
            icon = Icons.Rounded.Title
        ),
        PlayerOption(
            title = context.getString(R.string.size).toFirstCap(),
            id = AppConstants.SORT_TYPE_BY_SIZE,
            icon = Icons.Rounded.FileOpen
        ),
        PlayerOption(
            title = context.getString(R.string.last_modified).toFirstCap(),
            id = AppConstants.SORT_TYPE_BY_LAST_MODIFIED,
            icon = Icons.Rounded.Alarm
        )
    )

    override val viewBy: List<PlayerOption> = listOf(
        PlayerOption(
            title = context.getString(R.string.gridview).toFirstCap(),
            id = AppConstants.GRID_VIEW,
            icon = Icons.Outlined.GridView
        ),
        PlayerOption(
            title = context.getString(R.string.medium_listview).toFirstCap(),
            id = AppConstants.MEDIUM_LIST_VIEW,
            icon = Icons.Outlined.List
        ),
        PlayerOption(
            title = context.getString(R.string.small_listview).toFirstCap(),
            id = AppConstants.SMALL_LIST_VIEW,
            icon = Icons.Rounded.Menu
        ),
        PlayerOption(
            title = context.getString(R.string.large_listview).toFirstCap(),
            id = AppConstants.LARGE_LIST_VIEW,
            icon = Icons.Rounded.Alarm
        )
    )

    override val settingThemes: List<PlayerOption>
        get() = listOf(
            PlayerOption(
                title = context.getString(R.string.use_system_theme).toFirstCap(),
                id = AppConstants.THEME_FOLLOW_SYSTEM,
                icon = Icons.Rounded.Info
            ),
            PlayerOption(
                title = context.getString(R.string.light_theme).toFirstCap(),
                id = AppConstants.THEME_LIGHT,
                icon = Icons.Rounded.Info
            ),
            PlayerOption(
                title = context.getString(R.string.dark_theme).toFirstCap(),
                id = AppConstants.THEME_DARK,
                icon = Icons.Rounded.Info
            )
        )

    override val itemMenu: List<PlayerOption>
        get() = listOf(
            PlayerOption(
                title = context.getString(R.string.play).toFirstCap(),
                id = AppConstants.PLAY,
                icon = Icons.Rounded.PlayCircle
            ),
            /*SortOption(
                title = "Date",
                id = AppConstants.SORT_TYPE_BY_DATE,
                icon = Icons.Rounded.DateRange
            ),*/
            PlayerOption(
                title = context.getString(R.string.delete_video).toFirstCap(),
                id = AppConstants.DELETE,
                icon = Icons.Rounded.Delete
            ),
            PlayerOption(
                title = context.getString(R.string.video_details).toFirstCap(),
                id = AppConstants.VIDEO_DETAILS,
                icon = Icons.Rounded.Info
            )
        )

    override val allVideos: String
        get() = context.getString(R.string.all_videos)
    override val videosNotFound: String
        get() = context.getString(R.string.video_are_not_found)
    override val detailsNotFound: String
        get() = context.getString(R.string.details_not_found).toFirstCap()
    override val deleted: String
        get() = context.getString(R.string.deleted)
    override val somethingWentWrong: String
        get() = context.getString(R.string.something_went_wrong)
    override val watchAgain: String
        get() = context.getString(R.string.watch_again)
    override val watch: String
        get() = context.getString(R.string.watch)
    override val failedToWatchVideo: String
        get() = context.getString(R.string.failed_to_watch)
    override val failedToWatchMsg: String
        get() = context.getString(R.string.failed_to_watch_msg)
    override val pleaseWait: String
        get() = context.getString(R.string.please_wait)
    override val blockAds: String
        get() = context.getString(R.string.block_ads)
    override val successForOneDay: String
        get() = context.getString(R.string.success_for_one_day)
    override val videoAdsIsNotAvailable: String
        get() = context.getString(R.string.video_ads_is_not_available)
}