package com.abdulrahman.ecommerce.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.adapters.HomeFragmentViewPagerAdapter
import com.abdulrahman.ecommerce.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = HomeFragmentViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Laptop"
                2 -> tab.text = "Mobile"
                3 -> tab.text = "Tv"
                4 -> tab.text = "Head Phone"
            }
        }.attach()

        binding.viewPager.isUserInputEnabled = false

    }

    override fun onResume() {
        super.onResume()
        showBottomViewNavigationBar()
    }

    private fun showBottomViewNavigationBar() {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE
    }

}
