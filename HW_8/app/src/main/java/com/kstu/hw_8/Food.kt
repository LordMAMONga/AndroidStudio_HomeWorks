package com.kstu.hw_8

data class Food(
    val imageUrl: String,
    val name: String,
    val price: String,
    val currency: String,
    var isSelected: Boolean = false

)
