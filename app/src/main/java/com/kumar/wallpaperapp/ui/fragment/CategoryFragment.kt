package com.kumar.wallpaperapp.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.kumar.wallpaperapp.data.Category
import com.kumar.wallpaperapp.databinding.FragmentCategoryBinding
import com.kumar.wallpaperapp.ui.activity.ImagesListActivity
import com.kumar.wallpaperapp.ui.activity.MainActivity
import com.kumar.wallpaperapp.ui.adapter.CategoryListAdapter
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel
import com.kumar.wallpaperapp.utils.Coroutines

class CategoryFragment: Fragment() {

    private lateinit var viewModel: WallpaperViewModel
    private  var binding: FragmentCategoryBinding? = null
    var mActivity:Activity? = null
    var isLoading:Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryBinding.inflate(inflater,container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isSubCategory: Boolean?
        isSubCategory = arguments?.getBoolean("isSubCategory", false)
        if (isSubCategory == null) {
            isSubCategory = false
        }

        val categoryId:Category? = if (isSubCategory) {
             arguments?.getSerializable("category") as Category
        } else null

        if (activity is MainActivity) {
            mActivity = activity as MainActivity
            val act:MainActivity = mActivity!! as MainActivity
            viewModel = act.viewModel
        }
        else if (activity is ImagesListActivity) {
            mActivity = activity as ImagesListActivity
            val act:ImagesListActivity = mActivity!! as ImagesListActivity
            viewModel = act.viewModel
        }

        val categoryListAdapter = if (isSubCategory)
            CategoryListAdapter(mActivity!!, true)
        else
             CategoryListAdapter(mActivity!!)
        val categoryList: MutableLiveData<List<Category>> = MutableLiveData()

        binding?.categoryList?.layoutManager = GridLayoutManager(activity,2)
        binding?.categoryList?.adapter = categoryListAdapter

        categoryList.observe(viewLifecycleOwner, { categoryList ->
            categoryListAdapter.setCategory(categoryList)
            binding?.catProgressBar?.visibility = View.GONE
        })

        if (!isSubCategory)
        {
            binding?.catProgressBar?.visibility = View.VISIBLE
            Coroutines.main {
                val category = viewModel.getCategory()
                categoryList.postValue(category)
            }
        }
        else {
            binding?.catProgressBar?.visibility = View.VISIBLE
            Coroutines.main {
                val category = viewModel.getSubCategory(categoryId?.id!!)
                categoryList.postValue(category)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            mActivity = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}