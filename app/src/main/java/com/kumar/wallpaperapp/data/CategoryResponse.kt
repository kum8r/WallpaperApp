package com.kumar.wallpaperapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryResponse(
    @SerializedName("categories")
    val Categories: List<Category>?,
    @SerializedName("sub-categories")
    val SubCategories: List<Category>?,
    val success: Boolean
) : Serializable