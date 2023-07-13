package com.abdulrahman.ecommerce.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.abdulrahman.ecommerce.fragments.categories.HeadPhoneCategoryFragment
import com.abdulrahman.ecommerce.fragments.categories.LaptopCategoryFragment
import com.abdulrahman.ecommerce.fragments.categories.MainCategoryFragment
import com.abdulrahman.ecommerce.fragments.categories.MobileCategoryFragment
import com.abdulrahman.ecommerce.fragments.categories.TvCategoryFragment
import dagger.hilt.android.ViewModelLifecycle

class HomeFragmentViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {

    private val fragments = listOf(
        MainCategoryFragment(),
        LaptopCategoryFragment(),
        MobileCategoryFragment(),
        TvCategoryFragment(),
        HeadPhoneCategoryFragment()
    )
    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}