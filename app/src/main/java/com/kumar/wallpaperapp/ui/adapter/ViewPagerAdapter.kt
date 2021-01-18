package com.kumar.wallpaperapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kumar.wallpaperapp.ui.fragment.FeaturedImagesFragment
import com.kumar.wallpaperapp.ui.fragment.NewestImagesFragment
import com.kumar.wallpaperapp.ui.fragment.PopularImagesFragment

class ViewPagerAdapter(fm:FragmentManager)
    :FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> NewestImagesFragment()
            1 -> FeaturedImagesFragment()
            else -> PopularImagesFragment()
        }
    }
}

