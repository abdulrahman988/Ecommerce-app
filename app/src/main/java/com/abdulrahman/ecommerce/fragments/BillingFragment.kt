package com.abdulrahman.ecommerce.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.adapters.AddressRecyclerViewAdapter
import com.abdulrahman.ecommerce.adapters.CheckoutProductRecyclerViewAdapter
import com.abdulrahman.ecommerce.databinding.FragmentBillingBinding
import com.abdulrahman.ecommerce.paging.PagingAdapter
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.BillingViewModel
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val viewModel by viewModels<BillingViewModel>()
    private lateinit var addressAdapter: AddressRecyclerViewAdapter
    private lateinit var checkoutProductsAdapter: CheckoutProductRecyclerViewAdapter
    private lateinit var request: StringRequest


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBillingBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddressRecyclerView()
        setupCheckoutProductsRecyclerView()

        lifecycleScope.launch {
            viewModel.productPrice.collect {
                binding.tvTotalprice.text = "$ ${String.format("%.2f", (it))}"
            }
        }

        lifecycleScope.launch {
            viewModel.address.collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { it1 -> addressAdapter.submitList(it1) }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()

                    }

                    else -> {}
                }
            }
        }
        lifecycleScope.launch {
            viewModel.product.collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { it1 -> checkoutProductsAdapter.submitList(it1) }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {}
                }
            }
        }
        //implementing stripe payment
        request = StringRequest(Request.Method.POST,"")



































    }

    private fun setupAddressRecyclerView() {
        addressAdapter = AddressRecyclerViewAdapter()
        binding.rvAdresses.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = addressAdapter
        }
    }

    private fun setupCheckoutProductsRecyclerView() {
        checkoutProductsAdapter = CheckoutProductRecyclerViewAdapter()
        binding.rvProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = checkoutProductsAdapter
        }
    }
}