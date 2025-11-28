package com.kstu.myapplication;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kstu.myapplication.databinding.ActivityMain2Binding;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private ArrayList<Food> foods;
    private FoodAdapter adapter;
    private View selectedView = null;
    private Drawable originalBackground = null;

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

        foods = new ArrayList<>();
        foods.add(new Food("Burger", '$', 15, "https://cdn.apartmenttherapy.info/image/upload/f_auto,q_auto:eco,c_fill,g_auto,w_1500,ar_3:2/tk%2Fphoto%2F2025%2F06-2025%2F2025-06-burger-doneness-guide%2Fburger-doneness-guide-0195"));
        foods.add(new Food("Pizza", '$', 20, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQx5dPOz2sz7SjRSUKFhiovqRFfwVX5DWZwDg&s"));
        foods.add(new Food("Chicken", '$', 17, "https://recipes.timesofindia.com/thumb/61589069.cms?width=1200&height=900"));
        foods.add(new Food("Coca Cola", '$', 10, "https://blogdapublicidade.com/wp-content/uploads/2024/04/historia-logotipo-coca-cola.jpg"));

        adapter = new FoodAdapter(foods);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this, LinearLayoutManager.HORIZONTAL, false));

        binding.recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(MainActivity2.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(childView);
                    if (position == RecyclerView.NO_POSITION) {
                        return false;
                    }
                    Food clickedFood = foods.remove(position);
                    foods.add(0, clickedFood);
                    adapter.notifyItemMoved(position, 0);
                    rv.scrollToPosition(0);
                    rv.post(() -> {
                        RecyclerView.ViewHolder holder = rv.findViewHolderForAdapterPosition(0);
                        if (holder != null) {
                            View newSelectedView = holder.itemView;
                            if (selectedView != null && selectedView != newSelectedView) {
                                selectedView.setBackground(originalBackground);
                                selectedView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
                            }
                            if (selectedView != newSelectedView) {
                                originalBackground = newSelectedView.getBackground();
                                newSelectedView.setBackgroundColor(Color.RED);
                                newSelectedView.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                                selectedView = newSelectedView;
                            }
                        }
                    });

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

}
