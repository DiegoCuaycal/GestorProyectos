package com.diegocuaycal.gestorproyectos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrearProyectoActivity extends AppCompatActivity {

    EditText etNombreProyecto, etDescripcion, etFechaInicio, etFechaFin;
    Button btnGuardar;
    DBHelper dbHelper;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proyecto);
        String nombreUsuario = getIntent().getStringExtra("usuario");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Obtener el ID del usuario logueado
        Cursor cursor = db.rawQuery("SELECT id FROM Usuarios WHERE usuario = ?", new String[]{nombreUsuario});
        if (cursor.moveToFirst()) {
            idUsuario = cursor.getInt(0);
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
        cursor.close();

        // Vincular vistas
        etNombreProyecto = findViewById(R.id.etNombreProyecto);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        btnGuardar = findViewById(R.id.btnGuardarProyecto);

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombreProyecto.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String fechaInicio = etFechaInicio.getText().toString().trim();
            String fechaFin = etFechaFin.getText().toString().trim();

            if (nombre.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                Toast.makeText(this, "Nombre y fechas son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id_usuario", idUsuario);
            values.put("nombre", nombre);
            values.put("descripcion", descripcion);
            values.put("fecha_inicio", fechaInicio);
            values.put("fecha_fin", fechaFin);

            long resultado = writableDB.insert("Proyectos", null, values);

            if (resultado != -1) {
                Toast.makeText(this, "Proyecto guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar proyecto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
