package com.abdulrahman.ecommerce.fragments.shopping

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdulrahman.ecommerce.adapters.CartRecyclerViewAdapter
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.databinding.FragmentCartBinding
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartRecyclerViewAdapter: CartRecyclerViewAdapter
    private val viewModel by viewModels<CartViewModel>()

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
        viewModel.getTotalPrice()
        setupRecyclerView()

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

        lifecycleScope.launch {
            viewModel.price.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.tvTotalprice.text = "$ ${String.format("%.2f", (it.data))}"
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.cartProduct.collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data?.isEmpty() == true) {
                            showEmptyCart()
                        } else {
                            cartRecyclerViewAdapter.submitList(it.data!!)

                        }
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

    private fun showEmptyCart() {
        binding.apply {
            rvCart.visibility = View.INVISIBLE
            textView.visibility = View.INVISIBLE
            tvTotalprice.visibility = View.INVISIBLE
            btnCheckout.visibility = View.INVISIBLE

            emptyCart.visibility = View.VISIBLE
            tvEmptyCart.visibility = View.VISIBLE

        }
    }

    private fun setupRecyclerView() {
        cartRecyclerViewAdapter = CartRecyclerViewAdapter(CartRecyclerViewAdapter.OnClickListener {
            viewModel.deleteCartProduct(it)
        })
        binding.rvCart.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartRecyclerViewAdapter
        }
    }


    companion object {
        const val TAG = "cart fragment"
    }
}