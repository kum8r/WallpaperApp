package com.kumar.wallpaperapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kumar.wallpaperapp.data.Category
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.network.APIService
import com.kumar.wallpaperapp.respository.Repository
import com.kumar.wallpaperapp.utils.APIException
import com.kumar.wallpaperapp.utils.Coroutines
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WallpaperViewModel(private val repository: Repository) : ViewModel() {
//    var newestWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()
//    var popularWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()
//    var featuredWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()
//    var searchedWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()
//    var categoryList: MutableLiveData<List<Category>> = MutableLiveData()
//    var categoryWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()
//    var subCategoryWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()
//    var subCategoryList: MutableLiveData<List<Category>> = MutableLiveData()

//    init {
//        getCategory()
//        getFeaturedWallpaperList(1)
//        getPopularWallpaperList(1)
//    }



     suspend fun getNewestWallpaperList(pageNo:Int) : List<Wallpapers> {
         return repository.getNewestWallpapers(pageNo)
    }

    suspend fun getPopularWallpaperList(pageNo: Int) : List<Wallpapers>{
       return repository.getPopularWallpaper(pageNo)
    }

    suspend fun getFeaturedWallpaperList(pageNo: Int) : List<Wallpapers> {
        return repository.getFeaturedWallpaper(pageNo)
    }

    suspend fun searchWallpaper(searchText:String, pageNo: Int) : List<Wallpapers> {
        return repository.getSearchWallpaper(searchText, pageNo)
    }

    suspend fun getCategoryWallpaper(id:Int, pageNo: Int) : List<Wallpapers> {
        return repository.getCategoryWallpaper(id, pageNo)
    }

    suspend fun getCategory() : List<Category> {
        return repository.getCategoryList()
    }

    suspend fun getSubCategory(id:Int): List<Category> {
        return repository.getSubCategoryList(id)
    }

    suspend fun getSubCategoryWallpaper(id:Int, pageNo: Int) : List<Wallpapers> {
       return  repository.getSubCategoryWallpaper(id, pageNo)
    }

}
