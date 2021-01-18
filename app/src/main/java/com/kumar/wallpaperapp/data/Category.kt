package com.kumar.wallpaperapp.data

import java.io.Serializable

data class Category    (
    val count: Int,
    val id: Int,
    val name: String,
    val url: String
) : Serializable