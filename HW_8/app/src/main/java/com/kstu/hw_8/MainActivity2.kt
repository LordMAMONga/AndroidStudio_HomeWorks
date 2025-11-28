package com.kstu.hw_8

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kstu.hw_8.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity(), OnFoodItemClickListener {

    private lateinit var foodAdapter: FoodAdapter
    private lateinit var binding : ActivityMain2Binding
    private val foods = mutableListOf<Food>()
    private var itemPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        foods.addAll(loadFoods())
        foodAdapter = FoodAdapter(foods, this)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity2, LinearLayoutManager.HORIZONTAL, false)
            adapter = foodAdapter
        }


    }

    override fun onFoodItemClick(position: Int) {

        if(itemPosition != -1&& itemPosition< foods.size){
            foods[itemPosition].isSelected = false
        }
        val clickedFood = foods[position]
        clickedFood.isSelected = true
        foods.removeAt(position)
        foods.add(0, clickedFood)
        itemPosition = 0
        foodAdapter.notifyDataSetChanged()
        binding.recyclerView.scrollToPosition(0)

    }

    private fun loadFoods(): List<Food> {
        return listOf(
            Food(imageUrl = "https://cdn.apartmenttherapy.info/image/upload/f_auto,q_auto:eco,c_fill,g_auto,w_1500,ar_3:2/tk%2Fphoto%2F2025%2F06-2025%2F2025-06-burger-doneness-guide%2Fburger-doneness-guide-0195", name = "Burger", price = "15", currency = "$"),
            Food(imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQx5dPOz2sz7SjRSUKFhiovqRFfwVX5DWZwDg&s", name = "Pizza", price = "20", currency = "$"),
            Food(imageUrl = "https://recipes.timesofindia.com/thumb/61589069.cms?width=1200&height=900", name = "Chicken", price = "17", currency = "$"),
            Food(imageUrl = "https://blogdapublicidade.com/wp-content/uploads/2024/04/historia-logotipo-coca-cola.jpg", name = "Coca Cola", price = "10", currency = "$")
        )
    }
}