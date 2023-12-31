package com.abdulrahman.ecommerce.fragments.categories

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.adapters.BestDealsRecyclerViewAdapter
import com.abdulrahman.ecommerce.adapters.BestProductRecyclerViewAdapter
import com.abdulrahman.ecommerce.adapters.SpecialProductRecyclerViewAdapter
import com.abdulrahman.ecommerce.databinding.FragmentMainCategoryBinding
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductRecyclerViewAdapter
    private lateinit var bestDealsAdapter: BestDealsRecyclerViewAdapter
    private lateinit var bestProductsAdapter: BestProductRecyclerViewAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRecyclerView()
        setupBestDealsRecyclerView()
        setupBestProductsRecyclerView()

        lifecycleScope.launch {
            viewModel.specialProducts.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        specialProductsAdapter.submitList(it.data!!)
                        hideProgressBar()
                    }

                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.bestDeals.collect {
                when (it) {
                    is Resource.Success -> {
                        bestDealsAdapter.submitList(it.data!!)
                    }

                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }


        lifecycleScope.launch {
            viewModel.bestProducts.collect {
                when (it) {
                    is Resource.Success -> {
                        bestProductsAdapter.submitList(it.data!!)
                    }

                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }


    }

    private fun setupSpecialProductRecyclerView() {
        specialProductsAdapter =
            SpecialProductRecyclerViewAdapter(SpecialProductRecyclerViewAdapter.OnClickListener {

                val bundle = Bundle().apply {
                    putParcelable("product", it)
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_productDetailsFragment, bundle
                )
            })
        binding.rvSpecialProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }
    }

    private fun setupBestDealsRecyclerView() {
        bestDealsAdapter =
            BestDealsRecyclerViewAdapter(BestDealsRecyclerViewAdapter.OnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("product", it)
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_productDetailsFragment, bundle
                )
            })
        binding.rvBestDeals.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun setupBestProductsRecyclerView() {
        bestProductsAdapter =
            BestProductRecyclerViewAdapter(BestProductRecyclerViewAdapter.OnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("product", it)
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_productDetailsFragment, bundle
                )
            })
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.GONE
            rvSpecialProducts.visibility = View.VISIBLE
            tvBestDeals.visibility = View.VISIBLE
            rvBestDeals.visibility = View.VISIBLE
            tvBestProducts.visibility = View.VISIBLE
            rvBestProducts.visibility = View.VISIBLE
        }
    }
}