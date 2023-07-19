package com.abdulrahman.ecommerce.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.data.Product
import com.abdulrahman.ecommerce.databinding.BestDealsRvItemBinding
import com.abdulrahman.ecommerce.databinding.CartProductItemBinding
import com.bumptech.glide.Glide

class CartRecyclerViewAdapter : RecyclerView.Adapter<CartRecyclerViewAdapter.cartViewHolder>() {

    private var items: List<CartProduct> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):cartViewHolder {
        val binding = CartProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class cartViewHolder(private val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imgCartProduct)
                tvCartProductName.text = cartProduct.product.name
                tvProductCartPrice.text = cartProduct.product.price.toString()
                tvQuantity.text = cartProduct.quantity.toString()

            }
        }
    }

    fun submitList(list: List<CartProduct>) {
        val oldList = items
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ArticleItemDiffCallback(
                oldList, list
            )
        )
        items = list
        diffResult.dispatchUpdatesTo(this)
    }

    class ArticleItemDiffCallback(
        private var oldProductList: List<CartProduct>,
        private var newProductList: List<CartProduct>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldProductList.size
        }

        override fun getNewListSize(): Int {
            return newProductList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductList[oldItemPosition].product.id == newProductList[newItemPosition].product.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductList[oldItemPosition] == newProductList[newItemPosition]
        }
    }
}
