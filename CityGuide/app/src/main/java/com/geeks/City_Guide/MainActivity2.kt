package com.geeks.City_Guide

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.geeks.City_Guide.databinding.ActivityMain2Binding

class MainActivity2: AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("TITLE")
        val description = intent.getStringExtra("DESC")
        val imageUrl = intent.getStringExtra("IMAGE")
        val latitude = intent.getDoubleExtra("LAT",0.0)
        val longitude = intent.getDoubleExtra("LON",0.0)

        binding.tvDetailDescription.text = description
        binding.tvDetailTitle.text = title
        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .into(binding.ivDetailImage)

        binding.btnMap.setOnClickListener {
        }
    }
}