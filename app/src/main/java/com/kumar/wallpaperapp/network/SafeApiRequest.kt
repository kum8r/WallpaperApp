package com.kumar.wallpaperapp.network

import com.kumar.wallpaperapp.data.Category
import com.kumar.wallpaperapp.data.CategoryResponse
import com.kumar.wallpaperapp.data.WallpaperListResponse
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.utils.APIException

abstract class SafeApiRequest {

    suspend fun apiWallpaperRequest(call: suspend () -> WallpaperListResponse) : List<Wallpapers> {
        val response = call.invoke()

        if (response.success) {
            return response.wallpapers
        }
        else {
            throw APIException("Error")
        }
    }

    suspend fun apiCategoryRequest(call: suspend () -> CategoryResponse) : List<Category> {
        val response = call.invoke()

        if (response.success) {
            return response.Categories!!
        }
        else {
            throw APIException("Error")
        }
    }

    suspend fun apiSubCategoryRequest(call: suspend () -> CategoryResponse) : List<Category> {
        val response = call.invoke()

        if (response.success) {
            return response.SubCategories!!
        }
        else {
            throw APIException("Error")
        }
    }

}