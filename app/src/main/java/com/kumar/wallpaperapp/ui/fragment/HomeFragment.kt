package com.kumar.wallpaperapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumar.wallpaperapp.R
import com.kumar.wallpaperapp.data.Category
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.databinding.FragmentHomeBinding
import com.kumar.wallpaperapp.ui.activity.MainActivity
import com.kumar.wallpaperapp.ui.adapter.CategoryListAdapter
import com.kumar.wallpaperapp.ui.adapter.ImageListAdapter
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel
import com.kumar.wallpaperapp.utils.Coroutines
import com.kumar.wallpaperapp.utils.WallpaperType

class HomeFragment: Fragment()  {
    private lateinit var viewModel: WallpaperViewModel
    private  var homeBinding: FragmentHomeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater,container, false)
        return homeBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        viewModel = mainActivity.viewModel

         class ImageClickListener(val wallpaperType: WallpaperType) : ImageListAdapter.OnClickListener {
             override fun onItemClicked() {
                 val arg:Int = when (wallpaperType) {
                     WallpaperType.Newest -> 0
                     WallpaperType.Popular -> 1
                     WallpaperType.Featured -> 2
                 }
                 if (findNavController().currentDestination?.id == R.id.homeFragment) {
                     val directions = HomeFragmentDirections.actionHomeFragmentToImagesFragment(arg)
                     findNavController().navigate(directions)
                 }
             }
         }

        setUpSearchBar()
        getNewestWallpapers(mainActivity, ImageClickListener(WallpaperType.Newest))
        getFeaturedWallpapers(mainActivity, ImageClickListener(WallpaperType.Featured))
        getPopularWallpapers(mainActivity, ImageClickListener(WallpaperType.Popular))
        getCategory(mainActivity)
    }

    private fun setUpSearchBar() {
        homeBinding?.homeSearchBar?.setOnClickListener{
            if (findNavController().currentDestination?.id == R.id.homeFragment) {
                val directions = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                findNavController().navigate(directions)
            }
        }
    }

    private fun getNewestWallpapers(activity: MainActivity, onClickListener: ImageListAdapter.OnClickListener) {
        val topImagesAdapter = ImageListAdapter(activity, onClickListener,true)
        homeBinding?.homeTopImages?.adapter = topImagesAdapter
        homeBinding?.homeTopImages?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val newestWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()

        newestWallpaperList.observe(viewLifecycleOwner, {
            topImagesAdapter.setWallpaper(it)
            Log.e("TAG", "getNewestWallpapers: $it", )
        })

        Coroutines.main {
            val newestWallpapers = viewModel.getNewestWallpaperList(1)
            newestWallpaperList.postValue(newestWallpapers)
        }
    }

    private fun getFeaturedWallpapers(activity: MainActivity, onClickListener: ImageListAdapter.OnClickListener) {
        val featuredImagesAdapter = ImageListAdapter(activity, onClickListener,true)
        homeBinding?.homeFeaturedImages?.adapter = featuredImagesAdapter
        homeBinding?.homeFeaturedImages?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val featuredWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()

        featuredWallpaperList.observe(viewLifecycleOwner, {
            featuredImagesAdapter.setWallpaper(it)
        })

        Coroutines.main {
            val featuredWallpapers = viewModel.getFeaturedWallpaperList(1)
            featuredWallpaperList.postValue(featuredWallpapers)
        }
    }

    private fun getPopularWallpapers(activity: MainActivity, onClickListener: ImageListAdapter.OnClickListener) {
        val popularImagesAdapter = ImageListAdapter(activity, onClickListener,true)
        homeBinding?.homePopularImages?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val popularWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()
        homeBinding?.homePopularImages?.adapter = popularImagesAdapter

        popularWallpaperList.observe(viewLifecycleOwner, {
            popularImagesAdapter.setWallpaper(it)
        })

        Coroutines.main {
            val popularWallpapers = viewModel.getPopularWallpaperList(1)
            popularWallpaperList.postValue(popularWallpapers)
        }
    }

    private fun getCategory(activity: MainActivity) {
        val categoryListAdapter = CategoryListAdapter(activity)
        val categoryList: MutableLiveData<List<Category>> = MutableLiveData()
        homeBinding?.homeCategory?.adapter = categoryListAdapter
        homeBinding?.homeCategory?.layoutManager = GridLayoutManager(activity, 2)
        categoryList.observe(viewLifecycleOwner,  {categoryList ->
            categoryListAdapter.setCategory(categoryList)
        })

        Coroutines.main {
            val category = viewModel.getCategory()
            categoryList.postValue(category)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

}