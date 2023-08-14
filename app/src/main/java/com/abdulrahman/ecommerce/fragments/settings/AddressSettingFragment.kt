package com.abdulrahman.ecommerce.fragments.settings

import android.os.Bundle
import android.util.Log
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
import com.abdulrahman.ecommerce.adapters.AddressDetailsRecyclerViewAdapter
import com.abdulrahman.ecommerce.adapters.AddressRecyclerViewAdapter
import com.abdulrahman.ecommerce.databinding.FragmentAddressSettingBinding
import com.abdulrahman.ecommerce.fragments.shopping.CartFragment
import com.abdulrahman.ecommerce.paging.PagingAdapter
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.AddressSettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressSettingFragment : Fragment() {

    private lateinit var binding: FragmentAddressSettingBinding
    private val viewModel by viewModels<AddressSettingViewModel>()
    private lateinit var addressAdapter: AddressDetailsRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupAddressRecyclerView()

        binding.imgAddressAdd.setOnClickListener {
            findNavController().navigate(R.id.action_addressSettingFragment_to_addressFragment)
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
            viewModel.delete.collect {
                when (it) {
                    is Resource.Success -> {
                        Toast.makeText(
                            requireContext(), "address deleted successfully", Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Error -> {
                        Log.e(CartFragment.TAG, it.message.toString())
                    }

                    else -> Unit
                }
            }
        }


    }


    private fun setupAddressRecyclerView() {
        addressAdapter =
            AddressDetailsRecyclerViewAdapter(AddressDetailsRecyclerViewAdapter.OnClickListener {
                viewModel.deleteAddress(it)
            })
        binding.addressRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = addressAdapter

        }

    }

}