package com.abdulrahman.ecommerce.paging

import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulrahman.ecommerce.data.Product
import com.abdulrahman.ecommerce.databinding.CategoryProductRvItemBinding
import com.bumptech.glide.Glide

class PagingAdapter(private val onClickListener: OnClickListener) :
    PagingDataAdapter<Product, PagingAdapter.ProductViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CategoryProductRvItemBinding.inflate(layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: CategoryProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                try {

                    Glide.with(itemView).load(product.images[0]).into(ivProductImageCategory)
                } catch (e: Exception) {
                    Log.d("vv", e.message.toString())
                }
                tvProductNameCategory.text = product.name
                tvProductPriceCategory.text = "$ ${String.format("%.2f", (product.price))}"

                if (product.offerPercentage != 0.toFloat()) {
                    tvProductPriceCategory.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    binding.tvProductDiscountedPriceCategory.typeface = Typeface.DEFAULT_BOLD

                    val discounted = (product.offerPercentage?.times(product.price))!! / 100
                    tvProductDiscountedPriceCategory.text =
                        "$ ${String.format("%.2f", (product.price - discounted))}"
                    tvProductOfferPercentageCategory.text = "-${product.offerPercentage.toInt()}%"
                } else {
                    tvProductOfferPercentageCategory.visibility = View.INVISIBLE
                    tvProductDiscountedPriceCategory.visibility = View.INVISIBLE
                    binding.tvProductPriceCategory.typeface = Typeface.DEFAULT_BOLD

                }

                root.setOnClickListener {
                    onClickListener.onClick(product)
                }
            }

        }
    }

    class OnClickListener(val clickListener: (product: Product) -> Unit) {
        fun onClick(product: Product) = clickListener(product)
    }


}