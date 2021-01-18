package com.kumar.wallpaperapp.network

import com.kumar.wallpaperapp.data.CategoryResponse
import com.kumar.wallpaperapp.data.WallpaperInfoResponse
import com.kumar.wallpaperapp.data.WallpaperListResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "07c637348631ce7e9573cb6d562339a2"

interface APIService {
    @GET("get.php?auth=$API_KEY")
    suspend fun getWallpapers(
        @Query("method") wallpaperMethod: String,
        @Query("page") pageNo: Int
    ): WallpaperListResponse

    @GET("get.php?auth=$API_KEY&method=category_list")
    suspend fun getCategoryList(): CategoryResponse

    @GET("get.php?auth=$API_KEY&method=sub_category_list")
    suspend fun getSubCategoryList(@Query("id") id: Int): CategoryResponse

    @GET("get.php?auth=$API_KEY&method=category&info_level=2")
    suspend fun getCategoryWallpaper(@Query("id") id: Int, @Query("page") pageNo: Int): WallpaperListResponse

    @GET("get.php?auth=$API_KEY&method=sub_category&info_level=2")
    suspend fun getSubCategoryWallpaper(
        @Query("id") id: Int,
        @Query("page") pageNo: Int
    ): WallpaperListResponse

    @GET("get.php?auth=$API_KEY&method=search")
    suspend fun searchWallpaper(@Query("term") searchTerm: String, @Query("page") pageNo: Int): WallpaperListResponse

    @GET("get.php?auth=$API_KEY&method=wallpaper_info")
    suspend fun getWallpaperInfo(@Query("id") id: Int): WallpaperInfoResponse
}