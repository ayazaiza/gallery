package com.media.gallery.domain.sealedCls

sealed class PageItemViewType(val count: Int) {
    data class GridListView(val girdCount: Int = 5) : PageItemViewType(count = girdCount)
    data object SmallListView : PageItemViewType(count = 1)
    data object MediumListView : PageItemViewType(count = 1)
    data object LargeListView : PageItemViewType(count = 1)
}
