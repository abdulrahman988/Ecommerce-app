package com.abdulrahman.ecommerce.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.databinding.FragmentMainCategoryBinding
import com.abdulrahman.ecommerce.databinding.FragmentMobileCategoryBinding
import com.abdulrahman.ecommerce.paging.PagingAdapter
import com.abdulrahman.ecommerce.viewmodel.LaptopViewModel
import com.abdulrahman.ecommerce.viewmodel.MobileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MobileCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMobileCategoryBinding
    private val viewModel by viewModels<MobileViewModel>()
    private lateinit var mobileAdapter: PagingAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMobileCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductRecyclerView()

        lifecycleScope.launch {
            viewModel.mobile.collect{
                mobileAdapter.submitData(it)
            }
        }



    }

    private fun setupProductRecyclerView() {
        mobileAdapter = PagingAdapter()
        binding.rvMobile.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = mobileAdapter
        }
    }
}