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
import com.abdulrahman.ecommerce.databinding.FragmentTvCategoryBinding
import com.abdulrahman.ecommerce.paging.PagingAdapter
import com.abdulrahman.ecommerce.viewmodel.LaptopViewModel
import com.abdulrahman.ecommerce.viewmodel.TvViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvCategoryFragment : Fragment() {
    private lateinit var binding: FragmentTvCategoryBinding
    private val viewModel by viewModels<TvViewModel>()
    private lateinit var tvAdapter: PagingAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTvCategoryBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductRecyclerView()
        lifecycleScope.launch {
            viewModel.tv.collect{
                tvAdapter.submitData(it)
            }
        }

    }

    private fun setupProductRecyclerView() {
        tvAdapter = PagingAdapter()
        binding.rvTv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = tvAdapter
        }
    }
}