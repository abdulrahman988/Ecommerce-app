package com.abdulrahman.ecommerce.fragments.product

import android.graphics.Paint
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
import androidx.navigation.fragment.navArgs
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.databinding.FragmentProductBinding
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.ProductDetailViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch


class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private val args: ProductDetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<ProductDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        binding.apply {
            try {
                Glide.with(requireContext()).load(product.images[0]).into(ivProductImage)
            } catch (e: Exception) {
                Log.d("vv", e.message.toString())
            }
            tvProductName.text = product.name
            productPrice.text = "$ ${String.format("%.2f", (product.price))}"

            if (product.offerPercentage != 0.toFloat()) {
                productPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                val discounted = (product.offerPercentage?.times(product.price))!! / 100
                productDiscountedPrice.text =
                    "$ ${String.format("%.2f", (product.price - discounted))}"
                productOfferPercentage.text = "-${product.offerPercentage.toInt()}%"
            } else {
                productOfferPercentage.visibility = View.GONE
                productDiscountedPrice.visibility = View.GONE
            }
            productDescription.text = product.description
            productSize.text = "Sizes : ${product.sizes!![0]}"
            productColor.text = "Colors : ${product.colors!![0]}"

            ivClose.setOnClickListener {
                findNavController().navigateUp()
            }

            btnAddToCart.setOnClickListener {
                viewModel.addUpdateProductInCart(CartProduct(product, 1))
            }
        }

        lifecycleScope.launch {
            viewModel.addToCart.collect {
                when (it) {
                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}

