package com.abdulrahman.ecommerce.fragments.product

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.databinding.FragmentProductBinding
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.ProductDetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thecode.aestheticdialogs.AestheticDialog
import com.thecode.aestheticdialogs.DialogAnimation
import com.thecode.aestheticdialogs.DialogStyle
import com.thecode.aestheticdialogs.DialogType
import com.thecode.aestheticdialogs.OnDialogClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
        hideBottomViewNavigationBar()

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
                    is Resource.Success -> {
                        addressAddedSuccefullyDialog()
                    }
                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }
    private fun hideBottomViewNavigationBar(){
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun addressAddedSuccefullyDialog() {
        AestheticDialog.Builder(requireActivity(), DialogStyle.FLAT, DialogType.SUCCESS)
            .setTitle("Success")
            .setMessage("Item Added to cart succefully")
            .setCancelable(false)
            .setDarkMode(false)
            .setGravity(Gravity.CENTER)
            .setAnimation(DialogAnimation.SHRINK)
            .setOnClickListener(object : OnDialogClickListener {
                override fun onClick(dialog: AestheticDialog.Builder) {
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_productDetailsFragment_to_cartFragment)
                }
            })
            .show()
    }
}

