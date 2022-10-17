package com.playlab.testingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.playlab.testingapp.data.local.ShoppingItem
import com.playlab.testingapp.databinding.ItemShoppingBinding
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {

    class ShoppingItemViewHolder(val itemBinding: ItemShoppingBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val itemImageBinding = ItemShoppingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ShoppingItemViewHolder(itemImageBinding)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.itemView.apply {
            glide.load(shoppingItem.imageUrl).into(holder.itemBinding.ivShoppingImage)

            holder.itemBinding.tvName.text = shoppingItem.name
            val amountText = "${shoppingItem.amount}x"
            holder.itemBinding.tvShoppingItemAmount.text = amountText
            val priceText = "${shoppingItem.price}€"
            holder.itemBinding.tvShoppingItemPrice.text = priceText
        }
    }
}