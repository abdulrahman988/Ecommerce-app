package com.abdulrahman.ecommerce.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulrahman.ecommerce.data.Product
import com.abdulrahman.ecommerce.databinding.BestDealsRvItemBinding
import com.bumptech.glide.Glide

class BestDealsRecyclerViewAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<BestDealsRecyclerViewAdapter.BestDealViewHolder>() {

    private var items: List<Product> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealViewHolder {
        val binding =
            BestDealsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestDealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BestDealViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class BestDealViewHolder(private val binding: BestDealsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(tvProductImage)
                tvProductName.text = product.name
                tvProductOriginalPrice.text = "$ ${String.format("%.2f", (product.price))}"
                tvProductOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                val discounted = (product.offerPercentage?.times(product.price))!! / 100

                tvProductDiscountedPrice.text =
                    "$ ${String.format("%.2f", (product.price - discounted))}"
                root.setOnClickListener {
                    onClickListener.onClick(product)
                }
            }
        }
    }

    fun submitList(list: List<Product>) {
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
        private var oldProductList: List<Product>,
        private var newProductList: List<Product>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldProductList.size
        }

        override fun getNewListSize(): Int {
            return newProductList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductList[oldItemPosition].id == newProductList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductList[oldItemPosition] == newProductList[newItemPosition]
        }
    }

    class OnClickListener(val clickListener: (product: Product) -> Unit) {
        fun onClick(product: Product) = clickListener(product)
    }
}