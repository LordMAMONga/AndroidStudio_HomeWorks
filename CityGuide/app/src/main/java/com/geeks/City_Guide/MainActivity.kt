package com.geeks.City_Guide

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.geeks.City_Guide.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), PlaceAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val places = createData()
        val adapter = PlaceAdapter(places,this)

        binding.recycle.layoutManager = LinearLayoutManager(this)
        binding.recycle.adapter = adapter

    }
    override fun onItemClick(place: Place){
        val intent = Intent(this, MainActivity2::class.java).apply {
            putExtra("TITLE", place.title)
            putExtra("DESC", place.description)
            putExtra("IMAGE", place.imageUrl)
            putExtra("LAT", place.latitude)
            putExtra("LON", place.longitude)
        }
        startActivity(intent)
    }
    private fun createData(): List<Place>{
        return listOf(
            Place(
                "Площадь Ала-Тоо",
                "Центральная площадь Бишкека, место проведения государственных мероприятий и народных гуляний.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwSyIpreL3bN4bLyUB4js6rzzLz6qduqdNFw&s",
                42.876, 74.604
            ),
            Place(
                "Ош базар",
                "Один из крупнейших рынков в Бишкеке, где можно найти всё: от продуктов до национальных сувениров.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRODwhUDTqb4ahdVOlX4saBTjYtDIyyQ4L-hw&s",
                42.877, 74.580
            ),
            Place(
                "Парк Ала-Арча",
                "Живописный национальный парк в горах Тянь-Шаня, в 40 км от Бишкека. Идеален для походов и пикников.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS5a20nzTYYEz099o6xe21dI5keW1XU7giDJg&s",
                42.555, 74.481
            ),
            Place(
                "Государственный исторический музей",
                "Главный музей Кыргызской Республики, содержит более 90 тысяч экспонатов истории и культуры.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWRoo4oh-RKzxv_D3jC4n32Pb0HNhN-1a-Ow&s",
                42.878, 74.604
            ),
            Place(
                "Площадь Победы",
                "Мемориальный комплекс в честь победы в Великой Отечественной войне, с вечным огнём в центре.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_xW2KAE8YfVPe3K5U_S-zKauytqBpd_ODZA&s",
                42.880, 74.619
            ),
            Place(
                "Дубовый парк",
                "Один из старейших парков города со столетними дубами, скульптурами под открытым небом и уютными аллеями.",
                "https://cdn-1.aki.kg/cdn-st-0/qS2/S/53277.1202eb05e832974a7dfe148a7bd2a811.jpg",
                42.879, 74.612
            )
        )
    }
}