package com.kumar.wallpaperapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kumar.wallpaperapp.databinding.FragmentImagesBinding
import com.kumar.wallpaperapp.ui.activity.MainActivity
import com.kumar.wallpaperapp.ui.adapter.ViewPagerAdapter
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel

class ImagesFragment: Fragment()  {
    private lateinit var viewModel: WallpaperViewModel
    private  var imagesBinding : FragmentImagesBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        imagesBinding = FragmentImagesBinding.inflate(inflater,container, false)
        return imagesBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        viewModel = mainActivity.viewModel

        imagesBinding?.imagesPager?.adapter = ViewPagerAdapter(childFragmentManager)

        val args:ImagesFragmentArgs by navArgs()
        val wallpaperType = args.WallpaperType
        changeViewPager(wallpaperType)
    }

    fun changeViewPager(pagerNo:Int) {
        imagesBinding?.imagesPager?.currentItem = pagerNo
    }
}
