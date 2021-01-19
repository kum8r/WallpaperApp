package com.kumar.wallpaperapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kumar.wallpaperapp.R
import com.kumar.wallpaperapp.data.Category
import com.kumar.wallpaperapp.databinding.ActivityImagesBinding
import com.kumar.wallpaperapp.network.NetworkConnectionInterceptor
import com.kumar.wallpaperapp.network.RetrofitInstance
import com.kumar.wallpaperapp.respository.Repository
import com.kumar.wallpaperapp.ui.fragment.CategoryFragment
import com.kumar.wallpaperapp.ui.fragment.ImagesListFragment
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModelFactory

class ImagesListActivity : AppCompatActivity() {
    private var binding: ActivityImagesBinding? = null
    lateinit var viewModel: WallpaperViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)

        viewModel = ViewModelProvider(
            this,
            WallpaperViewModelFactory(Repository(RetrofitInstance.invoke(networkConnectionInterceptor)))
        ).get(WallpaperViewModel::class.java)

        val category = intent.getSerializableExtra("category") as Category
        var isSubCategoryImageList: Boolean = intent.getBooleanExtra("isSubCategoryImageList", false)

        var isImageList = false
        isImageList = intent.getBooleanExtra("isImageList", false)
        var isCategoryList = false
        intent.getBooleanExtra("isCategoryList", false).also { isCategoryList = it }

        var fragment: Fragment? = null

        if (isCategoryList) {
            fragment = CategoryFragment()
        }

        if (isImageList) {
            fragment = ImagesListFragment()
        }
        changeFragment(category, fragment!!, isSubCategoryImageList)

    }


    fun changeFragment(category: Category, fragment: Fragment, isSubCategoryImageList:Boolean = false) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putBoolean("isSubCategory", true)
        bundle.putBoolean("isSubCategoryImageList", isSubCategoryImageList)
        bundle.putSerializable("category", category)
        fragment.arguments = bundle
        fragmentTransaction.add(R.id.images_fragment_container, fragment)
            .commit()
    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}