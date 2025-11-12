package com.kstu.hw_3;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
public class MainActivity extends AppCompatActivity {
    private TextView textView;
    int x,y;
    boolean plus,minus,multiply,divide;
    private boolean isOperationClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView = findViewById(R.id.text_view);
        initListeners();

    }

    private void initListeners() {
        findViewById(R.id.btn_0).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_1).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_2).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_3).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_4).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_5).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_6).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_7).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_8).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_9).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_divide).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_multiply).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_minus).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_plus).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_equal).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_percent).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_charge).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_ac).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn_dot).setOnClickListener(this::onNumberClick);


    }
    private void onNumberClick(View view){
        String text = ((MaterialButton)view).getText().toString();
        if(text.equalsIgnoreCase("ac")) {
            textView.setText("0");
            x = 0;
            y = 0;
        }
        else if(textView.getText().toString().equals("0") || isOperationClick) {
            textView.setText(text);
        }
        else{
            textView.append(text);
        }
        isOperationClick = false;
    }
    private void onOperatorClick(View view){
        if(view.getId() == R.id.btn_plus){
            x = Integer.parseInt(textView.getText().toString());
            plus = true;
        }
        if(view.getId() == R.id.btn_minus){
            x = Integer.parseInt(textView.getText().toString());
            minus = true;
        }
        if(view.getId() == R.id.btn_multiply){
            x = Integer.parseInt(textView.getText().toString());
            multiply = true;
        }
        if(view.getId() == R.id.btn_divide){
            x = Integer.parseInt(textView.getText().toString());
            divide = true;
        }
        if(view.getId() == R.id.btn_equal && plus){
            y = Integer.parseInt(textView.getText().toString());
            int result = x + y;
            textView.setText(String.valueOf(result));
            plus = false;
        }
        if(view.getId() == R.id.btn_equal && minus){
            y = Integer.parseInt(textView.getText().toString());
            int result = x - y;
            textView.setText(String.valueOf(result));
            minus = false;
        }
        if(view.getId() == R.id.btn_equal && multiply){
            y = Integer.parseInt(textView.getText().toString());
            int result = x * y;
            textView.setText(String.valueOf(result));
            multiply = false;
        }
        if(view.getId() == R.id.btn_equal && divide){
            y = Integer.parseInt(textView.getText().toString());
            if(y == 0){
                textView.setText("Error");
                return;
            }
            int result = x / y;
            textView.setText(String.valueOf(result));
            divide = false;
        }
        isOperationClick = true;

    }
}