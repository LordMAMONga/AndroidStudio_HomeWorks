package com.kstu.hw_2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       EditText emailEditText = findViewById(R.id.email);
       EditText passwordEditText = findViewById(R.id.password);
       Button loginButton = findViewById(R.id.button);
       View mainLayout = findViewById(R.id.main);
       TextView text1 = findViewById(R.id.text1);
       TextView text2 = findViewById(R.id.info);
       TextView text3 = findViewById(R.id.text2);

        loginButton.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String emailInput = emailEditText.getText().toString();
                String passwordInput = passwordEditText.getText().toString();

                if (!emailInput.isEmpty() && !passwordInput.isEmpty()) {
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }
        };

        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        loginButton.setOnClickListener(v -> {
            String emailInput = emailEditText.getText().toString();
            String passwordInput = passwordEditText.getText().toString();

            if (emailInput.equalsIgnoreCase("admin") && passwordInput.equalsIgnoreCase("admin")) {
                Snackbar.make(mainLayout, "Вы успешно вошли!", Snackbar.LENGTH_LONG).show();

                emailEditText.animate().alpha(0f).setDuration(500).withEndAction(() -> {
                    emailEditText.setVisibility(View.GONE);
                });
                passwordEditText.animate().alpha(0f).setDuration(500).withEndAction(() -> {
                    passwordEditText.setVisibility(View.GONE);
                });
                loginButton.animate().alpha(0f).setDuration(500).withEndAction(() -> {
                    loginButton.setVisibility(View.GONE);
                });
                text1.animate().alpha(0f).setDuration(500).withEndAction(() -> {
                    text1.setVisibility(View.GONE);
                });
                text2.animate().alpha(0f).setDuration(500).withEndAction(() -> {
                    text2.setVisibility(View.GONE);
                });
                text3.animate().alpha(0f).setDuration(500).withEndAction(() -> {
                    text3.setVisibility(View.GONE);
                });

            } else {
                Snackbar.make(mainLayout, "Неверный логин или пароль", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
