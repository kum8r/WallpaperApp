package com.kumar.wallpaperapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.databinding.FragmentFeaturedImagesBinding
import com.kumar.wallpaperapp.databinding.FragmentSearchBinding
import com.kumar.wallpaperapp.ui.activity.MainActivity
import com.kumar.wallpaperapp.ui.adapter.ImageListAdapter
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel
import com.kumar.wallpaperapp.utils.Coroutines
import com.kumar.wallpaperapp.utils.WallpaperType

class FeaturedImagesFragment: Fragment()  {
    private lateinit var viewModel: WallpaperViewModel
    private var binding: FragmentFeaturedImagesBinding? = null
    var isLoading:Boolean =false
    var pageNo:Int = 1
    lateinit var linearLayoutManager : LinearLayoutManager
    lateinit var featuredImageListAdapter: ImageListAdapter
    private val featuredWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFeaturedImagesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        viewModel = mainActivity.viewModel
        featuredImageListAdapter = ImageListAdapter(mainActivity, imageFullWidth = true)
        linearLayoutManager = LinearLayoutManager(mainActivity)

        binding?.featuredImageList?.adapter = featuredImageListAdapter
        binding?.featuredImageList?.layoutManager = linearLayoutManager

        setRVScrollListener()

        featuredWallpaperList.observe(viewLifecycleOwner, {
            featuredImageListAdapter.setWallpaper(it)
            binding?.featuredImagesProgress?.visibility = View.GONE
        })

        loadMore()
    }

    private fun setRVScrollListener() {
        binding?.featuredImageList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = linearLayoutManager.childCount
                val pastVisibleCount = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                val totalCount = featuredImageListAdapter.itemCount

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
            val featuredWallpapers = viewModel.getFeaturedWallpaperList(pageNo)
            featuredWallpaperList.postValue(featuredWallpapers)
            isLoading = true
            binding?.featuredImagesProgress?.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}