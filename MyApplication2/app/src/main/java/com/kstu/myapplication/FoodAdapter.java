package com.kstu.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kstu.myapplication.databinding.ItemFoodHolderBinding;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ItemFoodViewHolder> {
    private ArrayList<Food> foods;

    public FoodAdapter(ArrayList<Food> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public ItemFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodHolderBinding binding = ItemFoodHolderBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false);
        ItemFoodViewHolder holder = new ItemFoodViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemFoodViewHolder holder, int position) {
        holder.bind(foods.get(position));

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ItemFoodViewHolder extends RecyclerView.ViewHolder {

        private final ItemFoodHolderBinding binding;

        public ItemFoodViewHolder(ItemFoodHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Food food){
            binding.tvName.setText(food.getName());
            binding.tvPrice.setText(String.valueOf(food.getPrice()));
            binding.tvCurrency.setText(String.valueOf(food.getCurrency()));
            Glide.with(super.itemView)
                    .load(food.getImageUrl())
                    .centerCrop()
                    .into(binding.tvImage);
        }
    }


}
