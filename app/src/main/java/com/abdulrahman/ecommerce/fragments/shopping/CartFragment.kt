package com.abdulrahman.ecommerce.fragments.shopping

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.adapters.CartRecyclerViewAdapter
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.databinding.FragmentCartBinding
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.CartViewModel
import com.abdulrahman.ecommerce.viewmodel.MainCategoryViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartRecyclerview: CartRecyclerViewAdapter
    private val viewModel by viewModels<CartViewModel>()
    private var total: Float = 0F
    private var _cartProduct = CartProduct()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getCartProduct()
        setupRecyclerView()

        lifecycleScope.launch {
            viewModel.cartProduct.collect {
                when (it) {
                    is Resource.Success -> {
                        cartRecyclerview.submitList(it.data!!)
                        for (item in it.data) {
                            total += if (item.product?.offerPercentage?.equals(0) == true) {
                                item.quantity!! * item.product.price
                            } else {
                                val discounted =
                                    (item.product?.offerPercentage?.times(item.product.price))!! / 100
                                item.quantity!! * (item.product.price - discounted)
                            }
                        }
                        binding.tvTotalprice.text = "$ ${String.format("%.2f", (total))}"
                    }

                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.delete.collect {
                when (it) {
                    is Resource.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "item deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                    }

                    else -> Unit
                }
            }
        }

        binding.ivCloseCart.setOnClickListener {
            findNavController().navigateUp()
        }


    }

    private fun setupRecyclerView() {
        cartRecyclerview = CartRecyclerViewAdapter(viewModel)
        binding.rvCart.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartRecyclerview
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val TAG = "cart fragment"
    }
}