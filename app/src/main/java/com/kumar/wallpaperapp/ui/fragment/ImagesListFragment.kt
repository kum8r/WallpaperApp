package com.kumar.wallpaperapp.ui.fragment

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumar.wallpaperapp.data.Category
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.databinding.FragmentImageListBinding
import com.kumar.wallpaperapp.ui.activity.ImagesListActivity
import com.kumar.wallpaperapp.ui.adapter.ImageListAdapter
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel
import com.kumar.wallpaperapp.utils.Coroutines

class ImagesListFragment : Fragment() {
    private lateinit var viewModel: WallpaperViewModel
    private  var imagesBinding : FragmentImageListBinding? = null
    var pageNo: Int = 1
    lateinit var adapter: ImageListAdapter
    lateinit var linearLayoutManager:LinearLayoutManager
    var isLoading = false
    private val wallpaperList: MutableLiveData<List<Wallpapers>> = MutableLiveData()
    var category:Category? = null
    var isSubCategoryImageList:Boolean? = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        imagesBinding = FragmentImageListBinding.inflate(inflater,container, false)
        return imagesBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as ImagesListActivity

        viewModel  = activity.viewModel
        adapter = ImageListAdapter(activity)
        linearLayoutManager = LinearLayoutManager(activity)

        imagesBinding?.catImageList?.layoutManager = linearLayoutManager
        imagesBinding?.catImageList?.adapter = adapter

        val bundle = arguments
        category = bundle?.getSerializable("category") as Category?
        isSubCategoryImageList = bundle?.getBoolean("isSubCategoryImageList",false)

        wallpaperList.observe(viewLifecycleOwner, {
            adapter.setWallpaper(it)
        })

        imagesBinding?.catImageList?.adapter = adapter
        if (isSubCategoryImageList!!) {
            imagesBinding?.subCategorybutton?.visibility = View.INVISIBLE
        }
        else {
            imagesBinding?.subCategorybutton?.visibility = View.VISIBLE
        }
        loadMore()
        categoryImageBtn()
        setRVScrollListener()

    }

    private fun setRVScrollListener() {
        imagesBinding?.catImageList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = linearLayoutManager.childCount
                val pastVisibleCount = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                val totalCount = adapter.itemCount

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
            val wallpaper =
            if (isSubCategoryImageList!!) {
                viewModel.getSubCategoryWallpaper(category!!.id , pageNo)
            }
            else {
                viewModel.getCategoryWallpaper(category!!.id, pageNo)
            }
            if (isSubCategoryImageList!!) {
                Log.e("TAG", "loadMore: subactegory")
            }            else {
                Log.e("TAG", "loadMore: cactegory")
            }
            wallpaperList.postValue(wallpaper)
        }

//        isLoading = true
    }

    private fun categoryImageBtn() {
        imagesBinding?.subCategorybutton?.setOnClickListener {
            if (category != null) {
                val activity = activity as ImagesListActivity
                activity.changeFragment(category!!, CategoryFragment(), true)
            }
        }
    }

}