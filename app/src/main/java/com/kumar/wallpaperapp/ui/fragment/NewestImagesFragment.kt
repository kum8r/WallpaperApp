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
import com.kumar.wallpaperapp.databinding.FragmentHomeBinding
import com.kumar.wallpaperapp.databinding.FragmentImagesBinding
import com.kumar.wallpaperapp.databinding.FragmentNewestImagesBinding
import com.kumar.wallpaperapp.databinding.FragmentPopularImagesBinding
import com.kumar.wallpaperapp.ui.activity.MainActivity
import com.kumar.wallpaperapp.ui.adapter.ImageListAdapter
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel
import com.kumar.wallpaperapp.utils.Coroutines
import com.kumar.wallpaperapp.utils.WallpaperType


class NewestImagesFragment: Fragment()  {
    private lateinit var viewModel: WallpaperViewModel
    private  var binding: FragmentNewestImagesBinding? = null
    var isLoading:Boolean =false
    var pageNo:Int = 1
    lateinit var linearLayoutManager : LinearLayoutManager
    lateinit var newestImageListAdapter: ImageListAdapter
    val newestWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewestImagesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        viewModel = mainActivity.viewModel
        newestImageListAdapter = ImageListAdapter(mainActivity, imageFullWidth = true)
        linearLayoutManager = LinearLayoutManager(mainActivity)

        binding?.newestImageList?.adapter = newestImageListAdapter
        binding?.newestImageList?.layoutManager = linearLayoutManager

        setRVScrollListener()


        newestWallpaperList.observe(viewLifecycleOwner, {
            newestImageListAdapter.setWallpaper(it)
        })

        Coroutines.main {
            val newestWallpapers = viewModel.getNewestWallpaperList(1)
            newestWallpaperList.postValue(newestWallpapers)
        }

    }


    private fun setRVScrollListener() {
        binding?.newestImageList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = linearLayoutManager.childCount
                val pastVisibleCount = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                val totalCount = newestImageListAdapter.itemCount

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
            val newestWallpapers = viewModel.getNewestWallpaperList(1)
            newestWallpaperList.postValue(newestWallpapers)
            isLoading = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}