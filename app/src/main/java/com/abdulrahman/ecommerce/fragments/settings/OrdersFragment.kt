package com.abdulrahman.ecommerce.fragments.settings

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
import com.abdulrahman.ecommerce.adapters.OrderRecyclerViewAdapter
import com.abdulrahman.ecommerce.databinding.FragmentOrdersBinding
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.OrdersViewModel
import com.abdulrahman.ecommerce.viewmodel.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private val viewModel by viewModels<OrdersViewModel>()

    private lateinit var ordersAdapter: OrderRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupOrdersRecyclerView()
        hideBottomViewNavigationBar()

        binding.imgOrderClose.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch {
            viewModel.orders.collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data?.isEmpty() == true) {
                            showEmptyOrderList()
                        } else {
                            ordersAdapter.submitList(it.data!!)
                        }
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

    private fun showEmptyOrderList() {
        binding.apply {
            tvEmptyOrders.visibility = View.VISIBLE
            ordersRv.visibility = View.INVISIBLE
        }
    }

    private fun setupOrdersRecyclerView() {
        ordersAdapter = OrderRecyclerViewAdapter()

        binding.ordersRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = ordersAdapter
        }
    }

    private fun hideBottomViewNavigationBar() {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.INVISIBLE
    }


}