package com.abdulrahman.ecommerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulrahman.ecommerce.data.Address
import com.abdulrahman.ecommerce.databinding.AddressRvItemBinding

class AddressRecyclerViewAdapter :
    RecyclerView.Adapter<AddressRecyclerViewAdapter.AddressViewHolder>() {

    private var items: List<Address> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            AddressRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AddressViewHolder(private val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address) {
            binding.tvAddress.text = address.addressTitle

        }
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