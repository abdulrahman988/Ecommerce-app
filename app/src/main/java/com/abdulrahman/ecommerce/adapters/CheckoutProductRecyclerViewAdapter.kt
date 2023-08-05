package com.abdulrahman.ecommerce.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulrahman.ecommerce.data.Address
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.databinding.CheckoutProductRvItemBinding
import com.bumptech.glide.Glide

class CheckoutProductRecyclerViewAdapter :
    RecyclerView.Adapter<CheckoutProductRecyclerViewAdapter.CheckoutProductViewHolder>() {

    private var items: List<CartProduct> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutProductViewHolder {
        val binding =
            CheckoutProductRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckoutProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CheckoutProductViewHolder(private val binding: CheckoutProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {

                Glide.with(itemView).load(cartProduct.product.images[0]).into(ivProductImage)
                tvProductName.text = cartProduct.product.name
                tvProductQuantity.text = cartProduct.quantity.toString()
                tvProductOriginalPrice.text = "$ ${String.format("%.2f", (cartProduct.product.price))}"

                if (cartProduct.product.offerPercentage != 0.toFloat()) {
                    tvProductOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    val discounted =
                        (cartProduct.product.offerPercentage?.times(cartProduct.product.price))!! / 100

                    tvProductDiscountedPrice.text =
                        "$ ${String.format("%.2f", (cartProduct.product.price - discounted))}"

                } else {
                    tvProductDiscountedPrice.visibility = View.GONE
                }
            }

        }
    }

    fun submitList(list: List<CartProduct>) {
        val oldList = items
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            CheckoutProductItemDiffCallback(
                oldList, list
            )
        )
        items = list
        diffResult.dispatchUpdatesTo(this)
    }

    class CheckoutProductItemDiffCallback(
        private var oldCartProductList: List<CartProduct>,
        private var newCartProductList: List<CartProduct>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldCartProductList.size
        }

        override fun getNewListSize(): Int {
            return newCartProductList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCartProductList[oldItemPosition].product.id == newCartProductList[newItemPosition].product.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCartProductList[oldItemPosition] == newCartProductList[newItemPosition]
        }

    }


}