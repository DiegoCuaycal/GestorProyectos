package com.diegocuaycal.gestorproyectos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail, etPassword, etName, etConfirmPassword;
    Button btnRegister;
    DBHelper dbHelper;
    TextView tvVolverLoginDesdeRegistro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new DBHelper(this);

        btnRegister.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkEmailExists(email)) {
                Toast.makeText(this, "Este usuario ya está registrado", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean insertado = dbHelper.insertUser(name, email, password);

            if (insertado) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });

        tvVolverLoginDesdeRegistro = findViewById(R.id.tvVolverLoginDesdeRegistro);

        tvVolverLoginDesdeRegistro.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });


    }
}

