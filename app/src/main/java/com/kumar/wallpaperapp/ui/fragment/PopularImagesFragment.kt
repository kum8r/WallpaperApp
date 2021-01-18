package com.kumar.wallpaperapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumar.wallpaperapp.R
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.databinding.FragmentFeaturedImagesBinding
import com.kumar.wallpaperapp.databinding.FragmentPopularImagesBinding
import com.kumar.wallpaperapp.ui.activity.MainActivity
import com.kumar.wallpaperapp.ui.adapter.ImageListAdapter
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel
import com.kumar.wallpaperapp.utils.Coroutines
import com.kumar.wallpaperapp.utils.WallpaperType

class PopularImagesFragment: Fragment()  {
    private lateinit var viewModel: WallpaperViewModel
    private  var binding: FragmentPopularImagesBinding? = null
    var isLoading:Boolean =false
    var pageNo:Int = 1
    lateinit var linearLayoutManager : LinearLayoutManager
    lateinit var popularImageListAdapter: ImageListAdapter
    val popularWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPopularImagesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        viewModel = mainActivity.viewModel
        popularImageListAdapter = ImageListAdapter(mainActivity)
        linearLayoutManager = LinearLayoutManager(mainActivity)

        binding?.popularImageList?.adapter = popularImageListAdapter
        binding?.popularImageList?.layoutManager = linearLayoutManager

        setRVScrollListener()


        popularWallpaperList.observe(viewLifecycleOwner, {
            popularImageListAdapter.setWallpaper(it)
        })

        Coroutines.main {
            val popularWallpapers = viewModel.getPopularWallpaperList(1)
            popularWallpaperList.postValue(popularWallpapers)
        }

    }


    private fun setRVScrollListener() {
        binding?.popularImageList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = linearLayoutManager.childCount
                val pastVisibleCount = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                val totalCount = popularImageListAdapter.itemCount

                if (!isLoading) {
                    if (pastVisibleCount + visibleItemCount >= totalCount) {
                        pageNo++
                        loadMore()
                    }
                }
            }
        })
    }

    fun loadMore() {
        Coroutines.main {
            val popularWallpapers = viewModel.getPopularWallpaperList(1)
            popularWallpaperList.postValue(popularWallpapers)
            isLoading = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}