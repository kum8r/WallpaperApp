package com.kumar.wallpaperapp.respository

import com.kumar.wallpaperapp.data.*
import com.kumar.wallpaperapp.network.APIService
import com.kumar.wallpaperapp.network.SafeApiRequest

class Repository(private val apiService: APIService) : SafeApiRequest() {

    suspend fun getNewestWallpapers(pageNo: Int): List<Wallpapers> {
        return apiWallpaperRequest { apiService.getWallpapers("newest", pageNo) }
    }

    suspend fun getFeaturedWallpaper(pageNo: Int) : List<Wallpapers> {
        return apiWallpaperRequest { apiService.getWallpapers("featured",pageNo) }
    }

    suspend fun getPopularWallpaper(pageNo: Int) : List<Wallpapers> {
        return apiWallpaperRequest { apiService.getWallpapers("popular", pageNo) }
    }

    suspend fun getCategoryWallpaper(categoryId: Int, pageNo: Int) : List<Wallpapers> {
        return apiWallpaperRequest { apiService.getCategoryWallpaper(categoryId, pageNo) }
    }

    suspend fun getCategoryList() : List<Category> {
        return apiCategoryRequest { apiService.getCategoryList() }
    }

    suspend fun getSubCategoryList(categoryId: Int) : List<Category> {
        return apiSubCategoryRequest{ apiService.getSubCategoryList(categoryId) }
    }

    suspend fun getSearchWallpaper(searchTerm : String, pageNo: Int) : List<Wallpapers> {
        return apiWallpaperRequest{ apiService.searchWallpaper(searchTerm, pageNo) }
    }

    suspend fun getWallpaperInfo(id:Int) : WallpaperInfoResponse {
        return apiService.getWallpaperInfo(id)
    }

    suspend fun getSubCategoryWallpaper(id:Int, pageNo: Int) : List<Wallpapers> {
        return apiWallpaperRequest{ apiService.getSubCategoryWallpaper(id, pageNo) }
    }
}