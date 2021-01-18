package com.kumar.wallpaperapp.ui.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumar.wallpaperapp.R
import com.kumar.wallpaperapp.data.Category
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.databinding.FragmentCategoryBinding
import com.kumar.wallpaperapp.databinding.FragmentSearchBinding
import com.kumar.wallpaperapp.ui.activity.MainActivity
import com.kumar.wallpaperapp.ui.adapter.ImageListAdapter
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel
import com.kumar.wallpaperapp.utils.Coroutines
import com.kumar.wallpaperapp.utils.WallpaperType
import kotlinx.coroutines.CoroutineScope

class SearchFragment: Fragment()  {

    private lateinit var viewModel: WallpaperViewModel
    private  var binding: FragmentSearchBinding? = null
    var searchText = ""
    var isLoading:Boolean =false
    var pageNo:Int = 1
    lateinit var linearLayoutManager : LinearLayoutManager
    lateinit var searchListAdapter : ImageListAdapter
    val searchWallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        viewModel = mainActivity.viewModel
        searchListAdapter = ImageListAdapter(mainActivity)
        linearLayoutManager = LinearLayoutManager(mainActivity)

        binding?.searchImageList?.adapter = searchListAdapter
        binding?.searchImageList?.layoutManager = linearLayoutManager

        setRVScrollListener()



        binding?.searchBar?.setOnEditorActionListener { p0, p1, p2 ->
            searchText = p0?.text.toString()
            Coroutines.main {
                val wallpaper = viewModel.searchWallpaper(searchText, 1)
                searchWallpaperList.postValue(wallpaper)
            }
            isLoading = true
            true
        }

        searchWallpaperList.observe(viewLifecycleOwner, {
            searchListAdapter.setWallpaper(it)
            isLoading = false
            showLoadingorEmpty()
        })
    }


    private fun setRVScrollListener() {
        binding?.searchImageList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = linearLayoutManager.childCount
                val pastVisibleCount = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                val totalCount = searchListAdapter.itemCount

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
        searchText = binding?.searchBar?.text.toString()
        Coroutines.main{
            val wallpaper = viewModel.searchWallpaper(searchText, pageNo)
            searchWallpaperList.postValue(wallpaper)
        }
        isLoading = true
    }

    fun showLoadingorEmpty() {
        if (searchListAdapter.getWallpaperListSize() == 0) {
            if (isLoading) {
                binding?.searchImageList?.visibility = View.INVISIBLE
                binding?.loadingTextSearchView?.visibility = View.VISIBLE
                binding?.loadingTextSearchView?.text = "Loading..."
            }
            else {
                binding?.searchImageList?.visibility = View.INVISIBLE
                binding?.loadingTextSearchView?.visibility = View.VISIBLE
                binding?.loadingTextSearchView?.text = "No Images"
            }
        }
        else {
            binding?.searchImageList?.visibility = View.VISIBLE
            binding?.loadingTextSearchView?.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}