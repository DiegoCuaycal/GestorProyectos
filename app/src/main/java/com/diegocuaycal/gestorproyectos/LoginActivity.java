package com.diegocuaycal.gestorproyectos;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 👉 Usuario único para prueba (cada ejecución creará uno nuevo)
        String nuevoUsuario = "test_" + System.currentTimeMillis(); // Usuario distinto cada vez
        ContentValues values = new ContentValues();
        values.put("usuario", nuevoUsuario);
        values.put("contrasena", "abc123");

        long res = db.insert("Usuarios", null, values);
        if (res != -1) {
            Log.d("DB", "Usuario insertado: " + nuevoUsuario);
        } else {
            Log.d("DB", "Error al insertar usuario");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
