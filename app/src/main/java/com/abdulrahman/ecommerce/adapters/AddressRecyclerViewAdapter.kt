package com.abdulrahman.ecommerce.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.data.Address
import com.abdulrahman.ecommerce.data.Product
import com.abdulrahman.ecommerce.databinding.AddressRvItemBinding
import com.google.rpc.context.AttributeContext.Resource

class AddressRecyclerViewAdapter(
    private val onClickListener: OnClickListener
    ) :
    RecyclerView.Adapter<AddressRecyclerViewAdapter.AddressViewHolder>() {

    private var items: List<Address> = emptyList()
    private var singleItemSelectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            AddressRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        if (singleItemSelectedPosition == position) {
            holder.bind(items[position])
            holder.itemView.setBackgroundResource(R.drawable.blue_background)
        } else {
            holder.bind(items[position])
            holder.itemView.setBackgroundResource(R.drawable.white_line)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AddressViewHolder(private val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address) {
            binding.tvAddress.text = address.addressTitle
            binding.root.setOnClickListener {
                onClickListener.onClick(address)
                setSingleSelection(adapterPosition)

            }
        }
    }

    class OnClickListener(val clickListener: (address: Address) -> Unit) {
        fun onClick(address: Address) = clickListener(address)
    }

    private fun setSingleSelection(adapterPosition: Int) {
        if (adapterPosition == RecyclerView.NO_POSITION) return
        notifyItemChanged(singleItemSelectedPosition)
        singleItemSelectedPosition = adapterPosition
        notifyItemChanged(singleItemSelectedPosition)

    }

    fun submitList(list: List<Address>) {
        val oldList = items
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            AddressItemDiffCallback(
                oldList, list
            )
        )
        items = list
        diffResult.dispatchUpdatesTo(this)
    }

    class AddressItemDiffCallback(
        private var oldProductList: List<Address>,
        private var newProductList: List<Address>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldProductList.size
        }

        override fun getNewListSize(): Int {
            return newProductList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductList[oldItemPosition].addressTitle == newProductList[newItemPosition].addressTitle
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductList[oldItemPosition] == newProductList[newItemPosition]
        }
    }


}