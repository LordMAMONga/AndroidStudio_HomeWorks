package com.geeks.City_Guide

import com.mapbox.maps.Style
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.geeks.City_Guide.databinding.ActivityMapBinding

class MapActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latitude = intent.getDoubleExtra("LAT", 0.0)
        val longitude = intent.getDoubleExtra("LON", 0.0)

        binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)






    }
}