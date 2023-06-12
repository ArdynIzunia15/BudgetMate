package com.example.expensetracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    MaterialButton btnMasuk, btnDaftar;
    TextInputEditText inputUsername, inputPassword;
    TextInputLayout layoutPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Change Notification Bar Color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.secondary));

        // Shared Prefs
        SharedPreferences sharedPreferences = getSharedPreferences("activeUserAccount", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", 0);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");

        if(userId != 0 && !username.equals("") && !password.equals("")){
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        }

        // Init Components
        databaseHandler = new DatabaseHandler(this);
        btnMasuk = findViewById(R.id.btnMasuk);
        btnDaftar = findViewById(R.id.btnDaftar);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        layoutPassword = findViewById(R.id.layoutPassword);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Database searching
                User user = new User();
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                user = databaseHandler.getUser(username);
                if(user == null || !user.getPassword().equals(password)){
                    Toast.makeText(LoginActivity.this, "Username atau password salah", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Save Active User
                    SharedPreferences sharedPreferences = getSharedPreferences("activeUserAccount", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userId", user.getId());
                    editor.putString("username", user.getUsername());
                    editor.putString("password", user.getPassword());
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.putExtra("username", user.getUsername());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}