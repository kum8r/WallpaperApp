package com.kumar.wallpaperapp.data

import java.io.Serializable

data class Wallpapers (
    val category: String,
    val category_id: String,
    val file_size: String,
    val file_type: String,
    val height: String,
    val id: String,
    val sub_category: String,
    val sub_category_id: String,
    val url_image: String,
    val url_page: String,
    val url_thumb: String,
    val user_id: String,
    val user_name: String,
    val width: String
) : Serializable