package com.kstu.myapplication;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kstu.myapplication.databinding.ActivityMain2Binding;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain2Binding binding = ActivityMain2Binding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.activityMain2, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<Food> foods = new ArrayList<>();
        foods.add(new Food("Burger", '$', 15, "https://cdn.apartmenttherapy.info/image/upload/f_auto,q_auto:eco,c_fill,g_auto,w_1500,ar_3:2/tk%2Fphoto%2F2025%2F06-2025%2F2025-06-burger-doneness-guide%2Fburger-doneness-guide-0195"));
        foods.add(new Food("Pizza", '$', 20, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQx5dPOz2sz7SjRSUKFhiovqRFfwVX5DWZwDg&s"));
        foods.add(new Food("Chicken", '$', 17, "https://recipes.timesofindia.com/thumb/61589069.cms?width=1200&height=900"));
        foods.add(new Food("Coca Cola", '$', 10, "https://blogdapublicidade.com/wp-content/uploads/2024/04/historia-logotipo-coca-cola.jpg"));

        FoodAdapter adapter = new FoodAdapter(foods);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this, LinearLayout.HORIZONTAL, false));

    }

}