package com.geeks.hw_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geeks.hw_1.databinding.ItemFilmBinding

class FilmAdapter(val filmList: List<FilmModel>) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilmViewHolder {
        return FilmViewHolder(
            ItemFilmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: FilmViewHolder,
        position: Int
    ) {
        holder.onBind((filmList[position]))
    }

    override fun getItemCount() = filmList.size
    class FilmViewHolder(private val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(filmModel: FilmModel) {
            binding.apply {
                tvName.text = filmModel.name
                tvRate.text = filmModel.rate
                imgFilm.loadImg(filmModel.img)
            }
        }
    }
}