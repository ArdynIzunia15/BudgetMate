package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHandler dbHandler;
    MaterialButton btnMasuk, btnDaftar;
    TextInputEditText inputUsername, inputPassword, inputKonfirmasiPassword, inputUang;
    TextInputLayout layoutKonfirmasiPassword, layoutInputUang, layoutUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Change Notification Bar Color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.secondary));

        // Init Components
        dbHandler = new DatabaseHandler(this);
        btnMasuk = findViewById(R.id.btnMasuk);
        btnDaftar = findViewById(R.id.btnDaftar);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputKonfirmasiPassword = findViewById(R.id.inputKonfirmasiPassword);
        layoutKonfirmasiPassword = findViewById(R.id.layoutKonfirmasiPassword);
        inputUang = findViewById(R.id.inputUang);
        layoutInputUang = findViewById(R.id.layoutInputUang);
        layoutUsername = findViewById(R.id.layoutUsername);

        ThousandFormatterTextWatcher textWatcher = new ThousandFormatterTextWatcher(inputUang);
        inputUang.addTextChangedListener(textWatcher);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                String konfirmasiPassword = inputKonfirmasiPassword.getText().toString();
                int starterBalance;


                // Konfirmasi Password
                if(!password.equals(konfirmasiPassword)){
                    layoutKonfirmasiPassword.setErrorEnabled(true);
                    layoutKonfirmasiPassword.setError("Password belom sama");
                }
                else{
                    // Konfirmasi username dah ada ato belom
                    if(dbHandler.getUser(username) != null){
                        layoutUsername.setErrorEnabled(true);
                        layoutUsername.setError("Username dah ada yang make");
                        inputUsername.requestFocus();
                    }
                    else{
                        layoutUsername.setErrorEnabled(false);
                        layoutUsername.setError(null);
                        layoutKonfirmasiPassword.setErrorEnabled(false);
                        layoutKonfirmasiPassword.setError(null);
                        // Starter Balance
                        if(inputUang.getText().toString().equals("")){
                            layoutInputUang.setErrorEnabled(true);
                            layoutInputUang.setError("Tolong diisi yah");
                        }
                        else{
                            layoutInputUang.setErrorEnabled(false);
                            layoutInputUang.setError(null);
                            starterBalance = Integer.parseInt(inputUang.getText().toString().replace(",", ""));
                            // Add record
                            User user = new User();
                            user.setUsername(username);
                            user.setPassword(password);
                            user.setBalance(starterBalance);
                            if(dbHandler.addRecordUserAccount(user) == -1){
                                Toast.makeText(RegisterActivity.this, "Registrasi gagal", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                }
            }
        });
    }
}