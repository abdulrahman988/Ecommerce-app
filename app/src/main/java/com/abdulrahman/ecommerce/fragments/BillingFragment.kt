package com.abdulrahman.ecommerce.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.adapters.AddressRecyclerViewAdapter
import com.abdulrahman.ecommerce.databinding.FragmentBillingBinding
import com.abdulrahman.ecommerce.paging.PagingAdapter
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.BillingViewModel
import kotlinx.coroutines.launch


class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val viewModel by viewModels<BillingViewModel>()
    private lateinit var addressAdapter: AddressRecyclerViewAdapter


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
    }

    private fun setupAddressRecyclerView() {
        addressAdapter = AddressRecyclerViewAdapter()
        binding.rvAdresses.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = addressAdapter
        }
    }
}