package com.diegocuaycal.gestorproyectos;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    EditText etCorreoRecuperacion;
    Button btnRecuperar;
    DBHelper dbHelper;
    TextView tvVolverLoginDesdeRecuperar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        etCorreoRecuperacion = findViewById(R.id.etCorreoRecuperacion);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        dbHelper = new DBHelper(this);
        tvVolverLoginDesdeRecuperar = findViewById(R.id.tvVolverLoginDesdeRecuperar);

        btnRecuperar.setOnClickListener(view -> {
            String correo = etCorreoRecuperacion.getText().toString().trim();

            if (correo.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo", Toast.LENGTH_SHORT).show();
                return;
            }

            String contrasena = dbHelper.obtenerContrasena(correo);
            if (contrasena != null) {
                Toast.makeText(this, "Tu contraseña es: " + contrasena, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            }
        });

        tvVolverLoginDesdeRecuperar.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
