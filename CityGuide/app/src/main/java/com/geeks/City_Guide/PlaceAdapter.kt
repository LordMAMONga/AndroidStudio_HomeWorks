package com.geeks.City_Guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geeks.City_Guide.databinding.ItemPlaceHolderBinding

class PlaceAdapter(
    private val places: List<Place>,
    private val listener:OnItemClickListener
): RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(place: Place)
    }

    inner class PlaceViewHolder(
        private val binding: ItemPlaceHolderBinding): RecyclerView
    .ViewHolder(binding.root){
        fun bind(place: Place){
            binding.tvTitle.text = place.title
            binding.tvDescription.text = place.description
            Glide.with(itemView.context)
                .load(place.imageUrl)
                .centerCrop()
                .into(binding.ivPlace)

            itemView.setOnClickListener {
                listener.onItemClick(place)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceViewHolder {
        val binding = ItemPlaceHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaceViewHolder,
        position: Int) {
       holder.bind(places[position])
    }

    override fun getItemCount(): Int {
        return places.size
    }
}