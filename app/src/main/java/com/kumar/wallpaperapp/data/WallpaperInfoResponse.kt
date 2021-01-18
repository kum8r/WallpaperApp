package com.kumar.wallpaperapp.data

data class WallpaperInfoResponse(
    val success: Boolean,
    val tags: List<Tag>,
    val wallpaper: WallpaperInfo
)