package com.abdulrahman.ecommerce.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.adapters.BestDealsRecyclerViewAdapter
import com.abdulrahman.ecommerce.adapters.SpecialProductRecyclerViewAdapter
import com.abdulrahman.ecommerce.databinding.FragmentLaptopCategoryBinding
import com.abdulrahman.ecommerce.paging.PagingAdapter
import com.abdulrahman.ecommerce.viewmodel.LaptopViewModel
import com.abdulrahman.ecommerce.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LaptopCategoryFragment : Fragment() {
    private lateinit var binding: FragmentLaptopCategoryBinding
    private val viewModel by viewModels<LaptopViewModel>()
    private lateinit var laptopAdapter: PagingAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLaptopCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductRecyclerView()

        lifecycleScope.launch {
            viewModel.laptop.collect {
                laptopAdapter.submitData(it)
            }
        }
    }


    private fun setupProductRecyclerView() {
        laptopAdapter = PagingAdapter(PagingAdapter.OnClickListener {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailsFragment, bundle
            )
        })
        binding.rvLaptop.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = laptopAdapter
        }
    }
}