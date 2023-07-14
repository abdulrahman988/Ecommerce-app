package com.abdulrahman.ecommerce.fragments.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.abdulrahman.ecommerce.databinding.FragmentProductBinding
import com.bumptech.glide.Glide


class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        binding.apply {

            Glide.with(requireContext()).load(product.images[0]).into(ivProductImage)
            tvProductName.text = product.name
            productPrice.text = "$ ${String.format("%.2f",(product.price))}"
//            productDiscountedPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            val discounted = (product.offerPercentage?.times(product.price))!! /100

            productDiscountedPrice.text = "$ ${String.format("%.2f",(product.price - discounted))}"
            productOfferPercentage.text = product.offerPercentage.toString()
            productDescription.text = product.description
            productSize.text = product.sizes!![0]
            productColor.text = product.colors!![0]

        }
    }
}

