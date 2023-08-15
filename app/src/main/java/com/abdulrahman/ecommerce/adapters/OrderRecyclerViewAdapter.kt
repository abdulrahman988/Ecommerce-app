package com.abdulrahman.ecommerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulrahman.ecommerce.data.Address
import com.abdulrahman.ecommerce.data.Order
import com.abdulrahman.ecommerce.databinding.AddressDetailsRvItemBinding
import com.abdulrahman.ecommerce.databinding.OrderRvItemBinding

class OrderRecyclerViewAdapter :
    RecyclerView.Adapter<OrderRecyclerViewAdapter.OrderViewHolder>() {

    private var items: List<Order> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            OrderRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class OrderViewHolder(private val binding: OrderRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                tvOrderId.text = "Order ID: ${order.orderId}"
                tvOrderCity.text = "${order.address.city}"
                tvOrderState.text = "${order.address.state}"
                tvOrderStreet.text = "${order.address.street}"
                tvOrderPrice.text = "${order.totalPrice}"
                tvOrderPaymentType.text = "${order.paymentType}"
                tvOrderDate.text = "${order.createdAt}"
            }
        }
    }


    fun submitList(list: List<Order>) {
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
        private var oldOrderList: List<Order>,
        private var newOrderList: List<Order>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldOrderList.size
        }

        override fun getNewListSize(): Int {
            return newOrderList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldOrderList[oldItemPosition] == newOrderList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldOrderList[oldItemPosition] == newOrderList[newItemPosition]
        }
    }
}