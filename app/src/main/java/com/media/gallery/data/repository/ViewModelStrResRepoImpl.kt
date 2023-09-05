package com.media.gallery.data.repository

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Dehaze
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.PhotoSizeSelectActual
import androidx.compose.material.icons.rounded.PlayCircle
import com.media.gallery.R
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.extensions.toFirstCap
import com.media.gallery.domain.models.BottomNavItem
import com.media.gallery.domain.models.OptionsMenu
import com.media.gallery.domain.repository.ViewModelStrResRepo
import com.media.gallery.domain.sealedCls.HomeBottomNavigation


class ViewModelStrResRepoImpl(
    private val context: Context
) : ViewModelStrResRepo {

    override val sortOptions: List<OptionsMenu> = listOf(
        OptionsMenu(
            title = context.getString(R.string.sort_option_az).toFirstCap(),
            id = AppConstants.SORT_TYPE_BY_NAME_A_TO_Z,
            painter = R.drawable.sort_alpha_down_a_to_z
        ),
        OptionsMenu(
            title = context.getString(R.string.sort_option_za).toFirstCap(),
            id = AppConstants.SORT_TYPE_BY_NAME_Z_TO_A,
            painter = R.drawable.sort_alpha_down_z_to_a
        ),
        OptionsMenu(
            title = context.getString(R.string.size).toFirstCap(),
            id = AppConstants.SORT_TYPE_BY_SIZE,
            icon = Icons.Rounded.InsertDriveFile
        ),
        OptionsMenu(
            title = context.getString(R.string.last_modified).toFirstCap(),
            id = AppConstants.SORT_TYPE_BY_LAST_MODIFIED,
            icon = Icons.Rounded.Alarm
        )
    )

    override val viewTypeOptions: List<OptionsMenu> = listOf(
        OptionsMenu(
            title = context.getString(R.string.gridview).toFirstCap(),
            id = AppConstants.GRID_VIEW,
            icon = Icons.Outlined.GridView
        ),
        OptionsMenu(
            title = context.getString(R.string.medium_listview).toFirstCap(),
            id = AppConstants.MEDIUM_LIST_VIEW,
            icon = Icons.Outlined.FormatListBulleted
        ),
        OptionsMenu(
            title = context.getString(R.string.small_listview).toFirstCap(),
            id = AppConstants.SMALL_LIST_VIEW,
            icon = Icons.Rounded.Dehaze
        ),
        OptionsMenu(
            title = context.getString(R.string.large_listview).toFirstCap(),
            id = AppConstants.LARGE_LIST_VIEW,
            icon = Icons.Rounded.PhotoSizeSelectActual
        )
    )

    override val settingThemesOptions: List<OptionsMenu>
        get() = listOf(
            OptionsMenu(
                title = context.getString(R.string.use_system_theme).toFirstCap(),
                id = AppConstants.THEME_FOLLOW_SYSTEM,
                icon = Icons.Rounded.Info
            ),
            OptionsMenu(
                title = context.getString(R.string.light_theme).toFirstCap(),
                id = AppConstants.THEME_LIGHT,
                icon = Icons.Rounded.Info
            ),
            OptionsMenu(
                title = context.getString(R.string.dark_theme).toFirstCap(),
                id = AppConstants.THEME_DARK,
                icon = Icons.Rounded.Info
            )
        )

    override val itemMenuOptions: List<OptionsMenu>
        get() = listOf(
            OptionsMenu(
                title = context.getString(R.string.play).toFirstCap(),
                id = AppConstants.PLAY,
                icon = Icons.Rounded.PlayCircle
            ),
            /*SortOption(
                title = "Date",
                id = AppConstants.SORT_TYPE_BY_DATE,
                icon = Icons.Rounded.DateRange
            ),*/
            OptionsMenu(
                title = context.getString(R.string.delete_video).toFirstCap(),
                id = AppConstants.DELETE,
                icon = Icons.Rounded.Delete
            ),
            OptionsMenu(
                title = context.getString(R.string.video_details).toFirstCap(),
                id = AppConstants.VIDEO_DETAILS,
                icon = Icons.Rounded.Info
            )
        )
    override val bottomItems: List<BottomNavItem> = listOf(
        BottomNavItem(
            title = context.getString(R.string.photos).toFirstCap(),
            activeIcon = Icons.Rounded.Image,
            inActiveIcon = Icons.Outlined.Image,
            routeName = HomeBottomNavigation.Photos.routeName,
            homeBottomNavigation = HomeBottomNavigation.Photos,
        ),
        BottomNavItem(
            title = context.getString(R.string.albums).toFirstCap(),
            activeIcon = Icons.Rounded.Folder,
            inActiveIcon = Icons.Outlined.Folder,
            routeName = HomeBottomNavigation.AllFiles.routeName,
            homeBottomNavigation = HomeBottomNavigation.AllFiles,
        ),
        BottomNavItem(
            title = context.getString(R.string.videos).toFirstCap(),
            activeIcon = Icons.Rounded.Movie,
            inActiveIcon = Icons.Outlined.Movie,
            routeName = HomeBottomNavigation.Videos.routeName,
            homeBottomNavigation = HomeBottomNavigation.Videos,
        )
    )
    override val allVideos: String
        get() = context.getString(R.string.all_videos)
    override val videosNotFound: String
        get() = context.getString(R.string.video_are_not_found).toFirstCap()
    override val filesNotFound: String
        get() = context.getString(R.string.files_are_not_found).toFirstCap()
    override val photosNotFound: String
        get() = context.getString(R.string.photos_are_not_found).toFirstCap()
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