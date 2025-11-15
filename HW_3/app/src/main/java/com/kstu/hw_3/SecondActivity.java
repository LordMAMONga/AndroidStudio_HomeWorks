package com.kstu.hw_3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class SecondActivity extends Activity {

    private boolean isHeartSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView text = findViewById(R.id.text_two);
        String result = getIntent().getStringExtra("result");
        if (result != null) {
            text.setText(result);
        } else {
            text.setText("0");
        }

        findViewById(R.id.btn_next).setOnClickListener(v -> {
            finishAffinity();
        });

        ImageButton heartButton = findViewById(R.id.heart_button);
        heartButton.setOnClickListener(v -> {
            isHeartSelected = !isHeartSelected;
            heartButton.setSelected(isHeartSelected);
        });

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
