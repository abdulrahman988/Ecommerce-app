package com.abdulrahman.ecommerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulrahman.ecommerce.data.Address
import com.abdulrahman.ecommerce.data.Product
import com.abdulrahman.ecommerce.databinding.AddressDetailsRvItemBinding
import com.abdulrahman.ecommerce.databinding.BestDealsRvItemBinding

class AddressDetailsRecyclerViewAdapter(
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<AddressDetailsRecyclerViewAdapter.AddressViewHolder>() {

    private var items: List<Address> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            AddressDetailsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class AddressViewHolder(private val binding: AddressDetailsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address) {
            binding.apply {
                tvAddressTitle.text = address.addressTitle
                tvAddressCity.text = address.city
                tvAddressState.text = address.state
                tvAddressStreet.text = address.street
                ivDelete.setOnClickListener {
                    onClickListener.onClick(address)
                }
            }
        }
    }


    fun submitList(list: List<Address>) {
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
        private var oldAddressList: List<Address>,
        private var newAddressList: List<Address>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldAddressList.size
        }

        override fun getNewListSize(): Int {
            return newAddressList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldAddressList[oldItemPosition] == newAddressList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldAddressList[oldItemPosition] == newAddressList[newItemPosition]
        }
    }

    class OnClickListener(val clickListener: (address: Address) -> Unit) {
        fun onClick(address: Address) = clickListener(address)
    }
}