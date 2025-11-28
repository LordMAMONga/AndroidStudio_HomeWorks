package com.kstu.hw_8

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kstu.hw_8.databinding.ItemFoodHolderBinding

interface OnFoodItemClickListener {
    fun onFoodItemClick(position: Int)
}

class FoodAdapter(
    private val foods: List<Food>,
    private val clickListener: OnFoodItemClickListener
) : RecyclerView.Adapter<FoodAdapter.ItemFoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFoodViewHolder {
        val binding =
            ItemFoodHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemFoodViewHolder, position: Int) {
        holder.bind(foods[position], position)
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    inner class ItemFoodViewHolder(private val binding: ItemFoodHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food, position1: Int) {
            binding.tvName.text = food.name
            binding.tvPrice.text = food.price
            binding.tvCurrency.text = food.currency

            Glide.with(binding.root.context)
                .load(food.imageUrl)
                .centerCrop()
                .into(binding.tvImage)

            binding.root.setOnClickListener {
                clickListener.onFoodItemClick(position1)
            }

            if (food.isSelected) {
                binding.root.setBackgroundColor(Color.RED)
                binding.root.translationZ = 16f
            } else {
                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, android.R.color.transparent))
                binding.root.translationZ = 0f
            }
        }
    }
}