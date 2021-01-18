package com.kumar.wallpaperapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kumar.wallpaperapp.respository.Repository

@Suppress("UNCHECKED_CAST")
class WallpaperViewModelFactory(val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  WallpaperViewModel(repository) as T
    }
}